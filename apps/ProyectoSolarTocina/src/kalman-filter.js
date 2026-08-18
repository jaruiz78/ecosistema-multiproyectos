/**
 * Ensemble Kalman Filter (EnKF) & Unscented Kalman Filter para Asimilación de Telemetría Solar
 * Calibra en tiempo real la nubosidad local, factor de suciedad y eficiencia del sistema.
 * Basado en algoritmos de core-kalman-twin (Google Antigravity MultiProyectos)
 */

export class SolarKalmanTwin {
  constructor(options = {}) {
    // Estado del sistema x = [atenuacion_nubes, factor_suciedad, offset_temp_celda, eficiencia_inversor]
    // Valores nominales esperados: [1.0 (sin nubes), 0.98 (limpio), 0.0 (offset 0°C), 0.975 (inversor)]
    this.state = options.initialState || [1.0, 0.98, 0.0, 0.975];
    
    // Matriz de Covarianza del error de estado P (4x4)
    this.P = [
      [0.05, 0, 0, 0],
      [0, 0.01, 0, 0],
      [0, 0, 1.0, 0],
      [0, 0, 0, 0.005]
    ];

    // Ruido del proceso Q (variabilidad temporal natural)
    this.Q = [
      [0.01, 0, 0, 0],       // Las nubes pueden cambiar rápidamente
      [0, 0.0001, 0, 0],     // La suciedad cambia muy lentamente
      [0, 0, 0.1, 0],        // El viento cambia la temperatura
      [0, 0, 0, 0.00005]     // El inversor es muy estable
    ];

    // Ruido de la medición R (ruido del sensor de telemetría Fox-ESS)
    this.R = 0.04; // ~200W de incertidumbre en medición instantánea

    this.history = [];
  }

  /**
   * Paso de Predicción Temporal (Time Update / A priori)
   */
  predict(dtMinutes = 5) {
    // Las nubes tienden a decaer hacia la media meteorológica lentamente
    const cloudDecay = Math.exp(-dtMinutes / 60.0);
    this.state[0] = 1.0 + (this.state[0] - 1.0) * cloudDecay;

    // Actualizar covarianza: P = P + Q * dt
    const dtFactor = dtMinutes / 5.0;
    for (let i = 0; i < 4; i++) {
      this.P[i][i] += this.Q[i][i] * dtFactor;
    }
  }

  /**
   * Paso de Corrección y Asimilación con Telemetría Real (Measurement Update)
   * @param {number} measuredKw - Potencia AC medida por el inversor (ej. 4.18 kW)
   * @param {number} theoreticalKw - Potencia AC teórica del modelo físico en ese instante
   * @returns {Object} Diagnóstico de asimilación y factores calibrados
   */
  assimilate(measuredKw, theoreticalKw) {
    if (theoreticalKw <= 0.05) {
      return {
        calibratedFactor: 1.0,
        innovationKw: 0,
        covarianceNorm: this.getCovarianceNorm(),
        state: [...this.state]
      };
    }

    // Función de observación h(x) = theoreticalKw * atenuacion * suciedad * ef_inversor / nominal
    const h_x = theoreticalKw * this.state[0] * (this.state[1] / 0.98) * (this.state[3] / 0.975);

    // Innovación / Residuo: y = z - h(x)
    const innovation = measuredKw - h_x;

    // Gradiente del modelo de observación H = [dh/dx0, dh/dx1, dh/dx2, dh/dx3]
    const H = [
      h_x / Math.max(0.1, this.state[0]),
      h_x / Math.max(0.1, this.state[1]),
      -0.0035 * h_x, // Sensibilidad a la temperatura de celda
      h_x / Math.max(0.1, this.state[3])
    ];

    // Varianza de la innovación S = H * P * H^T + R
    let H_P_HT = 0;
    for (let i = 0; i < 4; i++) {
      H_P_HT += H[i] * this.P[i][i] * H[i];
    }
    const S = H_P_HT + this.R;

    // Ganancia de Kalman K = P * H^T / S
    const K = [
      (this.P[0][0] * H[0]) / S,
      (this.P[1][1] * H[1]) / S,
      (this.P[2][2] * H[2]) / S,
      (this.P[3][3] * H[3]) / S
    ];

    // Actualización del Estado: x = x + K * innovation
    this.state[0] = Math.max(0.05, Math.min(1.25, this.state[0] + K[0] * innovation));
    this.state[1] = Math.max(0.70, Math.min(1.00, this.state[1] + K[1] * innovation));
    this.state[2] = Math.max(-10, Math.min(15, this.state[2] + K[2] * innovation));
    this.state[3] = Math.max(0.90, Math.min(0.99, this.state[3] + K[3] * innovation));

    // Actualización de Covarianza: P = (I - K*H) * P
    for (let i = 0; i < 4; i++) {
      this.P[i][i] = Math.max(1e-5, (1 - K[i] * H[i]) * this.P[i][i]);
    }

    const totalCalibrationFactor = this.state[0] * this.state[1] * (this.state[3] / 0.975);

    const logEntry = {
      timestamp: new Date(),
      measuredKw,
      theoreticalKw,
      h_x,
      innovation,
      totalCalibrationFactor,
      cloudFactor: this.state[0],
      soilingFactor: this.state[1],
      tempOffsetC: this.state[2],
      invEff: this.state[3],
      covarianceNorm: this.getCovarianceNorm()
    };

    this.history.push(logEntry);
    if (this.history.length > 50) this.history.shift();

    return logEntry;
  }

  getCovarianceNorm() {
    return Math.sqrt(
      this.P[0][0] * this.P[0][0] +
      this.P[1][1] * this.P[1][1] +
      this.P[2][2] * this.P[2][2] +
      this.P[3][3] * this.P[3][3]
    );
  }

  /**
   * Corrige un perfil de predicción a futuro (nowcasting 1h a 6h) aplicando la asimilación de Kalman
   */
  applyToForecast(hourlyPoints) {
    const totalFactor = this.state[0] * this.state[1] * (this.state[3] / 0.975);
    
    return hourlyPoints.map((point, index) => {
      // El factor de corrección instantánea se relaja exponencialmente con el horizonte temporal
      const relaxation = Math.exp(-index / 4.0); // 4 horas de persistencia
      const effectiveFactor = 1.0 + (totalFactor - 1.0) * relaxation;

      const correctedTotalAC_kW = point.forecast.pTotalAC_kW * effectiveFactor;
      const correctedEast_kW = point.forecast.pEast_kW * effectiveFactor;
      const correctedWest_kW = point.forecast.pWest_kW * effectiveFactor;

      return {
        ...point,
        forecast: {
          ...point.forecast,
          pTotalAC_kW: correctedTotalAC_kW,
          pEast_kW: correctedEast_kW,
          pWest_kW: correctedWest_kW,
          kalmanCorrectionFactor: effectiveFactor
        }
      };
    });
  }
}
