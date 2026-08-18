/**
 * Solar Engine - Algoritmos Físicos y Astronómicos
 * Calibrado con:
 * - 10x Jinko Solar 500Wp (5.00 kWp total)
 * - 2x Baterías Fox-ESS EP5 HV (10.36 kWh @ 192V)
 * - Inversor Sunworks / Fox-ESS 10 kW (Modbus TCP)
 * - Agua Caliente Sanitaria (ACS): Placas Solares Térmicas (0 W consumo eléctrico)
 * - Climatización: 2x Splits Daikin Inverter (Salón 35 m² + Dormitorio 16 m²)
 * - Vehículo Eléctrico: Omoda 7 SHS (18.7 kWh)
 */

export class SolarEngine {
  constructor(config = {}) {
    this.config = {
      lat: config.lat || 37.5942, // Tocina, Sevilla
      lon: config.lon || -5.7397,
      altitude: config.altitude || 31,
      
      // Paneles Oficiales: JINKO SOLAR 500W
      panelBrand: 'Jinko Solar',
      panelModel: 'Tiger Pro 500W',
      panelWp: config.panelWp || 500, // 500 Wp por placa (5.00 kWp total)
      tempCoeff: config.tempCoeff || -0.0030, // -0.30%/°C
      noct: config.noct || 45,
      albedo: config.albedo || 0.20,
      
      // String 1 (Este - 85°)
      stringEastPanels: config.stringEastPanels !== undefined ? config.stringEastPanels : 6, // 3.00 kWp
      stringEastAzimuth: config.stringEastAzimuth || 85,
      stringEastTilt: config.stringEastTilt || 20,

      // String 2 (Oeste - 265°)
      stringWestPanels: config.stringWestPanels !== undefined ? config.stringWestPanels : 4, // 2.00 kWp
      stringWestAzimuth: config.stringWestAzimuth || 265,
      stringWestTilt: config.stringWestTilt || 20,

      // Inversor: SUNWORKS / FOX-ESS Híbrido 10 kW
      inverterBrand: 'Sunworks / Fox-ESS',
      inverterMaxKw: config.inverterMaxKw || 10.0,
      inverterEfficiency: config.inverterEfficiency || 0.980,

      // Baterías: FOX-ESS EP5 High Voltage (10.36 kWh)
      batteryBrand: 'Fox-ESS',
      batteryModel: 'EP5 High-Voltage Li-ion',
      batteryVoltageNominalV: 192.0,
      batteryCapacityKwh: config.batteryCapacityKwh || 10.36,
      batteryMaxPowerKw: config.batteryMaxPowerKw || 5.18,
      batteryRoundTripEff: config.batteryRoundTripEff || 0.980,
      
      // Agua Caliente Sanitaria (ACS): Placas Solares Térmicas (Consumo eléctrico = 0 W)
      hasSolarThermalAcs: true,

      // Climatización: 2x Splits Daikin Inverter de Alta Eficiencia
      hvacLivingRoomM2: 35.0, // Salón (Daikin 3.5 kW térmico, ~850W pico, ~380W crucero)
      hvacBedroomM2: 16.0,    // Dormitorio (Daikin 2.5 kW térmico, ~600W pico, ~240W crucero)
      hvacLivingRoomEnabled: true,
      hvacBedroomEnabled: true,

      // Perfil Hogar Base sin termo eléctrico (solo electrónica, iluminación, frigo y cocina)
      baseHomeDailyKwh: config.baseHomeDailyKwh || 12.0,
      baseHomeLoadW: config.baseHomeLoadW || 550,

      // Vehículo Eléctrico: Omoda 7 SHS (PHEV)
      evEnabled: config.evEnabled !== undefined ? config.evEnabled : true,
      evBatteryCapacityKwh: config.evBatteryCapacityKwh || 18.7,
      evDailyNeedKwh: config.evDailyNeedKwh || 10.5,
      evMaxChargeKw: config.evMaxChargeKw || 3.7,
      evChargeMode: config.evChargeMode || 'solar_surplus',
      
      // Tarifas eléctricas
      energyPriceKwh: config.energyPriceKwh || 0.1178,
      feedInTariffKwh: config.feedInTariffKwh || 0.0800,
      gridPowerKw: config.gridPowerKw || 4.6
    };
  }

  updateConfig(newConfig) {
    this.config = { ...this.config, ...newConfig };
  }

  getJulianDay(date) {
    const time = date.getTime();
    return (time / 86400000) + 2440587.5;
  }

  calculateSolarPosition(date) {
    const rad = Math.PI / 180;
    const deg = 180 / Math.PI;

    const jd = this.getJulianDay(date);
    const n = jd - 2451545.0;

    let L = (280.460 + 0.9856474 * n) % 360;
    if (L < 0) L += 360;

    let g = (357.528 + 0.9856003 * n) % 360;
    if (g < 0) g += 360;

    const lambda = L + 1.915 * Math.sin(g * rad) + 0.020 * Math.sin(2 * g * rad);
    const epsilon = 23.439 - 0.0000004 * n;

    const sinDelta = Math.sin(epsilon * rad) * Math.sin(lambda * rad);
    const deltaRad = Math.asin(sinDelta);
    const deltaDeg = deltaRad * deg;

    let alphaRad = Math.atan2(Math.cos(epsilon * rad) * Math.sin(lambda * rad), Math.cos(lambda * rad));
    let alphaDeg = (alphaRad * deg + 360) % 360;

    const hours = date.getUTCHours() + date.getUTCMinutes() / 60 + date.getUTCSeconds() / 3600;
    const T = n / 36525.0;
    let gst = 280.46061837 + 360.98564736629 * n + 0.000387933 * T * T - (T * T * T) / 38710000;
    gst = (gst + 360) % 360;

    const lst = (gst + this.config.lon + 360) % 360;

    let H = (lst - alphaDeg + 360) % 360;
    if (H > 180) H -= 360;
    const HRad = H * rad;

    const latRad = this.config.lat * rad;

    const sinElev = Math.sin(latRad) * Math.sin(deltaRad) + Math.cos(latRad) * Math.cos(deltaRad) * Math.cos(HRad);
    const elevationRad = Math.asin(Math.max(-1, Math.min(1, sinElev)));
    const elevationDeg = elevationRad * deg;
    const zenithDeg = Math.max(0, 90 - elevationDeg);
    const zenithRad = zenithDeg * rad;

    const cosAz = (Math.sin(deltaRad) - Math.sin(latRad) * Math.sin(elevationRad)) / 
                  (Math.cos(latRad) * Math.cos(elevationRad) + 1e-6);
    let azDeg = Math.acos(Math.max(-1, Math.min(1, cosAz))) * deg;
    if (Math.sin(HRad) > 0) {
      azDeg = 360 - azDeg;
    }

    return {
      elevationDeg,
      zenithDeg,
      zenithRad,
      azimuthDeg: azDeg,
      declinationDeg: deltaDeg,
      hourAngleDeg: H,
      isDay: elevationDeg > 0
    };
  }

  calculateClearSkyIrradiance(solarPos, dayOfYear) {
    if (!solarPos.isDay) return { ghi: 0, dni: 0, dhi: 0 };

    const rad = Math.PI / 180;
    const zRad = solarPos.zenithRad;
    const cosZ = Math.cos(zRad);

    if (cosZ <= 0.01) return { ghi: 0, dni: 0, dhi: 0 };

    const I0 = 1367 * (1 + 0.033 * Math.cos((360 * dayOfYear / 365) * rad));
    const am = 1 / (cosZ + 0.50572 * Math.pow(96.07995 - solarPos.zenithDeg, -1.6364));
    const pressureRatio = Math.exp(-this.config.altitude / 8434.5);
    const amCorr = am * pressureRatio;

    const dni = Math.max(0, I0 * Math.pow(0.72, Math.pow(amCorr, 0.678)));
    const ghi = Math.max(0, 1098 * cosZ * Math.exp(-0.057 / cosZ));
    const dhi = Math.max(0, ghi - dni * cosZ);

    return {
      ghi: Math.round(ghi),
      dni: Math.round(dni),
      dhi: Math.round(Math.max(dhi, 0.1 * ghi))
    };
  }

  calculateAOI(solarPos, surfaceTiltDeg, surfaceAzimuthDeg) {
    if (!solarPos.isDay) return 90;

    const rad = Math.PI / 180;
    const deg = 180 / Math.PI;

    const alphaRad = solarPos.elevationDeg * rad;
    const gammaSRad = solarPos.azimuthDeg * rad;
    const betaRad = surfaceTiltDeg * rad;
    const gammaPRad = surfaceAzimuthDeg * rad;

    const cosTheta = Math.sin(alphaRad) * Math.cos(betaRad) + 
                     Math.cos(alphaRad) * Math.sin(betaRad) * Math.cos(gammaSRad - gammaPRad);

    const clampedCos = Math.max(-1, Math.min(1, cosTheta));
    return Math.acos(clampedCos) * deg;
  }

  calculatePOA(dni, dhi, ghi, solarPos, surfaceTiltDeg, surfaceAzimuthDeg) {
    if (!solarPos.isDay || (dni === 0 && dhi === 0 && ghi === 0)) {
      return { poaTotal: 0, poaDirect: 0, poaDiffuse: 0, poaReflected: 0, aoi: 90 };
    }

    const rad = Math.PI / 180;
    const betaRad = surfaceTiltDeg * rad;
    const aoi = this.calculateAOI(solarPos, surfaceTiltDeg, surfaceAzimuthDeg);
    const cosTheta = Math.cos(aoi * rad);

    const poaDirect = cosTheta > 0 ? dni * cosTheta : 0;
    const poaDiffuse = dhi * ((1 + Math.cos(betaRad)) / 2);
    const poaReflected = ghi * this.config.albedo * ((1 - Math.cos(betaRad)) / 2);

    const poaTotal = Math.max(0, poaDirect + poaDiffuse + poaReflected);

    return { poaTotal, poaDirect, poaDiffuse, poaReflected, aoi };
  }

  calculateStringPower(poaW_m2, numPanels, ambientTempC) {
    if (poaW_m2 <= 1) return 0;

    const totalPeakWp = numPanels * this.config.panelWp;
    const cellTemp = ambientTempC + (poaW_m2 / 800) * (this.config.noct - 20);
    const tempFactor = 1 + this.config.tempCoeff * (cellTemp - 25);
    const opticalLoss = 0.98;

    const powerDC_W = totalPeakWp * (poaW_m2 / 1000) * Math.max(0.72, tempFactor) * opticalLoss;
    return Math.max(0, powerDC_W);
  }

  /**
   * Modela el consumo eléctrico dinámico de los 2 splits Daikin Inverter
   * según la temperatura exterior de Tocina y la hora del día.
   */
  calculateHvacPowerW(hour, ambientTempC) {
    let powerW = 0;

    // 1. Split Salón (35 m²): Uso habitual de 13:00 a 23:00 h
    if (this.config.hvacLivingRoomEnabled && hour >= 13 && hour <= 23) {
      if (ambientTempC > 28) { // Modo refrigeración A/C
        const tempDiff = ambientTempC - 24;
        powerW += Math.min(950, 350 + tempDiff * 38);
      } else if (ambientTempC < 16) { // Modo bomba de calor invierno
        const tempDiff = 21 - ambientTempC;
        powerW += Math.min(850, 300 + tempDiff * 32);
      }
    }

    // 2. Split Dormitorio (16 m²): Uso nocturno / siesta (15:00 - 17:00 y 23:00 - 07:00)
    if (this.config.hvacBedroomEnabled && ((hour >= 15 && hour <= 17) || hour >= 23 || hour <= 7)) {
      if (ambientTempC > 27) {
        const tempDiff = ambientTempC - 24;
        powerW += Math.min(650, 220 + tempDiff * 25);
      } else if (ambientTempC < 15) {
        const tempDiff = 20 - ambientTempC;
        powerW += Math.min(550, 180 + tempDiff * 22);
      }
    }

    return Math.round(powerW);
  }

  calculateHourlyPoint(date, weatherData = {}) {
    const dayOfYear = Math.floor((date - new Date(date.getFullYear(), 0, 0)) / 86400000);
    const solarPos = this.calculateSolarPosition(date);

    // 1. CLEAR-SKY (Máximo Teórico Jinko 500W)
    const clearSky = this.calculateClearSkyIrradiance(solarPos, dayOfYear);
    const poaEastClear = this.calculatePOA(
      clearSky.dni, clearSky.dhi, clearSky.ghi, solarPos,
      this.config.stringEastTilt, this.config.stringEastAzimuth
    );
    const poaWestClear = this.calculatePOA(
      clearSky.dni, clearSky.dhi, clearSky.ghi, solarPos,
      this.config.stringWestTilt, this.config.stringWestAzimuth
    );

    const ambTemp = weatherData.temp !== undefined ? weatherData.temp : 25;

    const pEastClear_W = this.calculateStringPower(poaEastClear.poaTotal, this.config.stringEastPanels, ambTemp);
    const pWestClear_W = this.calculateStringPower(poaWestClear.poaTotal, this.config.stringWestPanels, ambTemp);
    const pTotalDCClear_W = pEastClear_W + pWestClear_W;
    const pACClear_kW = Math.min(
      this.config.inverterMaxKw,
      (pTotalDCClear_W / 1000) * this.config.inverterEfficiency
    );

    // 2. PREVISIÓN METEOROLÓGICA REAL
    let dniReal = weatherData.dni !== undefined ? weatherData.dni : clearSky.dni;
    let dhiReal = weatherData.dhi !== undefined ? weatherData.dhi : clearSky.dhi;
    let ghiReal = weatherData.ghi !== undefined ? weatherData.ghi : clearSky.ghi;

    if (weatherData.cloudCover !== undefined && weatherData.dni === undefined) {
      const cloudFactor = Math.max(0.08, 1 - (weatherData.cloudCover / 100) * 0.85);
      dniReal = clearSky.dni * Math.pow(cloudFactor, 1.8);
      dhiReal = clearSky.dhi * (0.4 + 0.6 * (weatherData.cloudCover / 100));
      ghiReal = clearSky.ghi * cloudFactor;
    }

    const poaEastReal = this.calculatePOA(
      dniReal, dhiReal, ghiReal, solarPos,
      this.config.stringEastTilt, this.config.stringEastAzimuth
    );
    const poaWestReal = this.calculatePOA(
      dniReal, dhiReal, ghiReal, solarPos,
      this.config.stringWestTilt, this.config.stringWestAzimuth
    );

    const pEastReal_W = this.calculateStringPower(poaEastReal.poaTotal, this.config.stringEastPanels, ambTemp);
    const pWestReal_W = this.calculateStringPower(poaWestReal.poaTotal, this.config.stringWestPanels, ambTemp);
    const pTotalDCReal_W = pEastReal_W + pWestReal_W;
    const pACReal_kW = Math.min(
      this.config.inverterMaxKw,
      (pTotalDCReal_W / 1000) * this.config.inverterEfficiency
    );

    const hvacW = this.calculateHvacPowerW(date.getHours(), ambTemp);

    return {
      date,
      hour: date.getHours(),
      solarPosition: solarPos,
      hvacPowerW: hvacW,
      clearSky: {
        ghi: clearSky.ghi,
        dni: clearSky.dni,
        dhi: clearSky.dhi,
        poaEast: poaEastClear.poaTotal,
        poaWest: poaWestClear.poaTotal,
        pEast_kW: pEastClear_W / 1000,
        pWest_kW: pWestClear_W / 1000,
        pTotalAC_kW: pACClear_kW
      },
      forecast: {
        temp: ambTemp,
        cloudCover: weatherData.cloudCover || 0,
        weatherCode: weatherData.weatherCode || 0,
        poaEast: poaEastReal.poaTotal,
        poaWest: poaWestReal.poaTotal,
        pEast_kW: pEastReal_W / 1000,
        pWest_kW: pWestReal_W / 1000,
        pTotalAC_kW: pACReal_kW
      }
    };
  }

  simulateBatteryDispatch(hourlyPoints, initialHomeBatteryKwh = 5.18, initialEvBatteryKwh = 5.0) {
    let currentHomeBatteryKwh = Math.min(this.config.batteryCapacityKwh, Math.max(0.5, initialHomeBatteryKwh));
    let currentEvBatteryKwh = Math.min(this.config.evBatteryCapacityKwh, initialEvBatteryKwh);
    const minReserveKwh = this.config.batteryCapacityKwh * 0.10;

    let evDeliveredTodayKwh = 0;
    const targetEvChargeKwh = this.config.evEnabled ? this.config.evDailyNeedKwh : 0;

    return hourlyPoints.map(point => {
      const hour = point.hour;
      const solarGenW = point.forecast.pTotalAC_kW * 1000;
      
      // Hogar Base + Climatización Daikin real
      const baseLoadW = this.getHourlyHomeLoad(hour);
      const hvacW = point.hvacPowerW || 0;
      const totalHomeLoadW = baseLoadW + hvacW;

      let netSolarW = solarGenW - totalHomeLoadW;
      let homeBatteryPowerW = 0;
      let evChargePowerW = 0;
      let gridExportW = 0;
      let gridImportW = 0;

      if (netSolarW > 0) {
        const roomInHomeBatteryKwh = this.config.batteryCapacityKwh - currentHomeBatteryKwh;
        const maxChargeKwh = Math.min(
          this.config.batteryMaxPowerKw,
          (netSolarW / 1000) * this.config.batteryRoundTripEff
        );
        const actualChargeKwh = Math.min(roomInHomeBatteryKwh, maxChargeKwh);

        currentHomeBatteryKwh += actualChargeKwh;
        homeBatteryPowerW = (actualChargeKwh / this.config.batteryRoundTripEff) * 1000;

        netSolarW -= homeBatteryPowerW;

        if (this.config.evEnabled && this.config.evChargeMode === 'solar_surplus' && evDeliveredTodayKwh < targetEvChargeKwh) {
          const maxEvChargerW = this.config.evMaxChargeKw * 1000;
          const availableSolarForEvW = Math.min(maxEvChargerW, netSolarW);

          if (availableSolarForEvW >= 1380) {
            evChargePowerW = availableSolarForEvW;
            const evChargeKwh = (evChargePowerW / 1000) * 0.92;
            currentEvBatteryKwh = Math.min(this.config.evBatteryCapacityKwh, currentEvBatteryKwh + evChargeKwh);
            evDeliveredTodayKwh += evChargeKwh;
            netSolarW -= evChargePowerW;
          }
        }

        gridExportW = Math.max(0, netSolarW);

      } else {
        const neededKwh = (-netSolarW) / 1000;
        const availableFromBatteryKwh = Math.max(0, currentHomeBatteryKwh - minReserveKwh);
        const dischargeKwh = Math.min(
          this.config.batteryMaxPowerKw,
          availableFromBatteryKwh,
          neededKwh / this.config.batteryRoundTripEff
        );

        currentHomeBatteryKwh -= dischargeKwh;
        homeBatteryPowerW = -dischargeKwh * this.config.batteryRoundTripEff * 1000;

        const remainingDeficitW = (-netSolarW) - (-homeBatteryPowerW);
        gridImportW = Math.max(0, remainingDeficitW);

        if (this.config.evEnabled && this.config.evChargeMode === 'night_valley' && (hour >= 0 && hour < 8) && evDeliveredTodayKwh < targetEvChargeKwh) {
          const maxEvChargerW = this.config.evMaxChargeKw * 1000;
          evChargePowerW = maxEvChargerW;
          const evChargeKwh = (evChargePowerW / 1000) * 0.92;
          currentEvBatteryKwh = Math.min(this.config.evBatteryCapacityKwh, currentEvBatteryKwh + evChargeKwh);
          evDeliveredTodayKwh += evChargeKwh;
          gridImportW += evChargePowerW;
        }
      }

      const homeBatterySoc = (currentHomeBatteryKwh / this.config.batteryCapacityKwh) * 100;
      const evBatterySoc = (currentEvBatteryKwh / this.config.evBatteryCapacityKwh) * 100;

      return {
        ...point,
        battery: {
          currentKwh: currentHomeBatteryKwh,
          socPercent: homeBatterySoc,
          powerW: homeBatteryPowerW,
          homeLoadW: totalHomeLoadW,
          baseLoadW,
          hvacW,
          gridExportW,
          gridImportW
        },
        ev: {
          enabled: this.config.evEnabled,
          currentKwh: currentEvBatteryKwh,
          socPercent: evBatterySoc,
          chargePowerW: evChargePowerW,
          deliveredTodayKwh: evDeliveredTodayKwh,
          kmRangeKm: (currentEvBatteryKwh / this.config.evBatteryCapacityKwh) * 95
        }
      };
    });
  }

  getHourlyHomeLoad(hour) {
    // Hogar sin termo eléctrico (ya que tienes placas solares térmicas)
    const baseCurve = [
      320, 280, 260, 260, 280, 320,
      450, 620, 580, 480, 450, 520,
      750, 920, 980, 850, 620, 550,
      620, 820, 980, 1050, 820, 480
    ];
    return baseCurve[hour] || this.config.baseHomeLoadW;
  }
}
