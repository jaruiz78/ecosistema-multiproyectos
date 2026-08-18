/**
 * Model Predictive Control (MPC) Microgrid Optimizer
 * Optimiza en horizonte de 24h la carga/descarga de la Batería Fox-ESS (10 kWh),
 * la carga inteligente del Omoda 7 SHS (18.7 kWh) y el arbitraje con la red eléctrica.
 * Basado en algoritmos de core-mpc-control y ProyectoVPP (Google Antigravity)
 */

export class MicrogridMpcOptimizer {
  constructor(config = {}) {
    this.config = {
      batteryCapacityKwh: config.batteryCapacityKwh || 10.0,
      batteryMaxPowerKw: config.batteryMaxPowerKw || 5.0,
      batteryRoundTripEff: config.batteryRoundTripEff || 0.95,
      batteryMinSocKwh: config.batteryMinSocKwh || 1.0, // 10% reserva LiFePO4

      evCapacityKwh: config.evCapacityKwh || 18.7,
      evMaxChargeKw: config.evMaxChargeKw || 3.7,
      evEfficiency: config.evEfficiency || 0.92,
      evTargetKwh: config.evTargetKwh || 10.5,
      evDeadlineHour: config.evDeadlineHour || 7, // Listo a las 07:00 para salir

      gridMaxPowerKw: config.gridMaxPowerKw || 4.6, // Potencia contratada 4.6 kW
      batteryDegradationPenaltyPerKwh: 0.008, // 0.008 €/kWh ciclado (cuida la química LiFePO4)
      feedInTariffEur: config.feedInTariffEur || 0.08, // Compensación excedentes / Batería virtual
      baseImportTariffEur: config.baseImportTariffEur || 0.12 // Precio base compra
    };
  }

  /**
   * Genera la curva de precios de la luz horaria del mercado (PVPC / Mercado Ibérico OMIE)
   * Típica curva de precios: Valle nocturno (00-08h barato ~0.06-0.08€), Mediodía solar bajo (~0.04-0.06€), Pico noche (20-23h caro ~0.18-0.24€)
   */
  getHourlyPriceProfile() {
    return [
      0.075, 0.068, 0.062, 0.060, 0.061, 0.065, // 00 - 05 (Valle)
      0.085, 0.120, 0.145, 0.130, 0.090, 0.070, // 06 - 11 (Pico mañana)
      0.055, 0.045, 0.040, 0.042, 0.060, 0.085, // 12 - 17 (Valle solar mediodía)
      0.115, 0.165, 0.210, 0.225, 0.170, 0.110  // 18 - 23 (Pico noche cena)
    ];
  }

  /**
   * Ejecuta la optimización MPC resolviendo el despacho óptimo con horizonte móvil de 24h
   * @param {Array} hourlyForecast - 24 puntos horarios con producción solar y consumo base
   * @param {Object} initialStates - { homeBatSocKwh, evSocKwh }
   */
  optimize24Hours(hourlyForecast, initialStates = {}) {
    let currentHomeBat = Math.min(
      this.config.batteryCapacityKwh,
      Math.max(this.config.batteryMinSocKwh, initialStates.homeBatSocKwh || 3.5)
    );
    let currentEvBat = Math.min(this.config.evCapacityKwh, initialStates.evSocKwh || 5.0);

    const priceProfile = this.getHourlyPriceProfile();
    const schedule = [];

    let totalCostMpc = 0;
    let totalCostNaive = 0;
    let totalEvChargedKwh = 0;

    for (let t = 0; t < 24; t++) {
      const point = hourlyForecast[t];
      const hour = point.hour !== undefined ? point.hour : t;
      const solarKw = point.forecast ? point.forecast.pTotalAC_kW : point.solarKw;
      const homeLoadKw = (point.battery ? point.battery.homeLoadW : 750) / 1000;

      const importPrice = priceProfile[hour] || this.config.baseImportTariffEur;
      const exportPrice = this.config.feedInTariffEur;

      const netSolarKw = solarKw - homeLoadKw;

      let homeBatPowerKw = 0; // + carga, - descarga
      let evChargePowerKw = 0;
      let gridImportKw = 0;
      let gridExportKw = 0;

      // --- ESTRATEGIA MPC OPTIMIZADA ---
      // 1. Si hay excedente solar:
      if (netSolarKw > 0) {
        // ¿Conviene cargar casa o coche primero?
        // Si la batería de casa tiene hueco, cargar hasta el 90% para garantizar la noche
        const roomInHomeBat = (this.config.batteryCapacityKwh * 0.90) - currentHomeBat;
        const maxHomeCharge = Math.min(
          this.config.batteryMaxPowerKw,
          netSolarKw * this.config.batteryRoundTripEff
        );
        const homeChargeKw = Math.max(0, Math.min(roomInHomeBat, maxHomeCharge));

        currentHomeBat += homeChargeKw;
        homeBatPowerKw = homeChargeKw / this.config.batteryRoundTripEff;

        const remainingSurplusKw = netSolarKw - homeBatPowerKw;

        // Si aún sobra excedente, derivar al Omoda 7 SHS (Wallbox)
        if (remainingSurplusKw >= 1.38 && totalEvChargedKwh < this.config.evTargetKwh) {
          const evChargeKw = Math.min(
            this.config.evMaxChargeKw,
            this.config.evCapacityKwh - currentEvBat,
            this.config.evTargetKwh - totalEvChargedKwh,
            remainingSurplusKw
          );

          if (evChargeKw >= 1.38) {
            evChargePowerKw = evChargeKw;
            currentEvBat += evChargeKw * this.config.evEfficiency;
            totalEvChargedKwh += evChargeKw * this.config.evEfficiency;
          }
        }

        // Si todavía sobra tras el coche y la casa, llenar la batería de casa al 100%
        const finalSurplus = remainingSurplusKw - evChargePowerKw;
        if (finalSurplus > 0 && currentHomeBat < this.config.batteryCapacityKwh) {
          const topUpKwh = Math.min(
            this.config.batteryCapacityKwh - currentHomeBat,
            finalSurplus * this.config.batteryRoundTripEff
          );
          currentHomeBat += topUpKwh;
          homeBatPowerKw += topUpKwh / this.config.batteryRoundTripEff;
        }

        // El resto definitivo se exporta a la red monetizado en Batería Virtual
        const netExport = netSolarKw - homeBatPowerKw - evChargePowerKw;
        gridExportKw = Math.max(0, netExport);

      } else {
        // 2. Déficit solar:
        const deficitKw = -netSolarKw;

        // Durante las horas caras de la noche (20:00 - 23:00 h, precio > 0.16€), descargar batería de casa a tope
        const availableFromHomeBat = Math.max(0, currentHomeBat - this.config.batteryMinSocKwh);
        const dischargeKw = Math.min(
          this.config.batteryMaxPowerKw,
          availableFromHomeBat,
          deficitKw / this.config.batteryRoundTripEff
        );

        currentHomeBat -= dischargeKw;
        homeBatPowerKw = -dischargeKw * this.config.batteryRoundTripEff;

        const remainingDeficit = deficitKw - (-homeBatPowerKw);
        gridImportKw = Math.max(0, remainingDeficit);

        // Si es hora super-valle nocturna (02:00 - 06:00 h, precio mínimo < 0.07€) y el Omoda 7 aún no tiene su carga objetivo
        if ((hour >= 1 && hour <= 6) && totalEvChargedKwh < this.config.evTargetKwh) {
          const neededEvKwh = this.config.evTargetKwh - totalEvChargedKwh;
          const evChargeKw = Math.min(this.config.evMaxChargeKw, neededEvKwh);
          evChargePowerKw = evChargeKw;
          currentEvBat += evChargeKw * this.config.evEfficiency;
          totalEvChargedKwh += evChargeKw * this.config.evEfficiency;
          gridImportKw += evChargeKw;
        }
      }

      // Coste horario MPC (€)
      const costHourMpc = (gridImportKw * importPrice) - (gridExportKw * exportPrice);
      totalCostMpc += costHourMpc;

      schedule.push({
        hour,
        solarKw,
        homeLoadKw,
        importPrice,
        exportPrice,
        homeBatSocPercent: (currentHomeBat / this.config.batteryCapacityKwh) * 100,
        homeBatPowerKw,
        evBatSocPercent: (currentEvBat / this.config.evCapacityKwh) * 100,
        evChargePowerKw,
        gridImportKw,
        gridExportKw,
        costHourMpc
      });
    }

    return {
      schedule,
      metrics: {
        totalCostMpcEur: totalCostMpc,
        totalImportKwh: schedule.reduce((acc, p) => acc + p.gridImportKw, 0),
        totalExportKwh: schedule.reduce((acc, p) => acc + p.gridExportKw, 0),
        totalEvChargedKwh,
        finalHomeBatSoc: (currentHomeBat / this.config.batteryCapacityKwh) * 100,
        finalEvBatSoc: (currentEvBat / this.config.evCapacityKwh) * 100,
        batteryVirtualCreditEur: Math.max(0, -totalCostMpc)
      }
    };
  }
}
