/**
 * Asistente Inteligente de Optimización de Electrodomésticos y Carga de Vehículo Eléctrico
 * Calcula las mejores franjas horarias (Coste 0.00 €) según excedentes solares y estado de batería.
 */

export const APPLIANCE_CATALOG = [
  {
    id: 'daikin_salon',
    name: 'Daikin Inverter Salón (35 m² - 25°C)',
    icon: '❄️',
    category: 'clima',
    durationHours: 6.0,
    powerKw: 0.52,
    totalEnergyKwh: 3.12,
    realPowerW: 520,
    description: 'Split Daikin Inverter en salón de 35 m². Confort continuo de día (13:00 - 20:00).'
  },
  {
    id: 'daikin_bedroom',
    name: 'Daikin Inverter Dormitorio (16 m² - 26.5°C)',
    icon: '🌙',
    category: 'clima',
    durationHours: 4.0,
    powerKw: 0.21,
    totalEnergyKwh: 0.84,
    realPowerW: 210,
    description: 'Split Daikin Inverter en dormitorio de 16 m². Modo silencioso siesta/noche.'
  },
  {
    id: 'midea_fridge',
    name: 'Frigorífico Midea 2 Puertas Side-by-Side Inox',
    icon: '🧊',
    category: 'cocina',
    durationHours: 24.0,
    powerKw: 0.09,
    totalEnergyKwh: 1.15,
    realPowerW: 90,
    description: 'Frigorífico americano Inox Midea Smart Home de doble puerta. Compresor Inverter continuo.'
  },
  {
    id: 'beko_washer',
    name: 'Lavadora BEKO 8 kg 1200 rpm (A++)',
    icon: '🧺',
    category: 'lavado',
    durationHours: 1.25,
    powerKw: 1.85,
    totalEnergyKwh: 0.85,
    realPowerW: 1850,
    description: 'Lavadora Beko A++ 8kg. Pico de calentamiento a 40°C y centrifugado a 1200 rpm.'
  },
  {
    id: 'fagor_dishwasher',
    name: 'Lavavajillas Fagor Innova Inox',
    icon: '🍽️',
    category: 'lavado',
    durationHours: 1.5,
    powerKw: 1.90,
    totalEnergyKwh: 1.10,
    realPowerW: 1900,
    description: 'Lavavajillas Fagor Innova Inox. Calentamiento de agua y ciclo de secado intensivo.'
  },
  {
    id: 'teka_oven',
    name: 'Horno Teka Multifunción Inox',
    icon: '🍳',
    category: 'cocina',
    durationHours: 0.75,
    powerKw: 2.20,
    totalEnergyKwh: 1.65,
    realPowerW: 2200,
    description: 'Horno eléctrico Teka multifunción con grill y convección.'
  },
  {
    id: 'cecofry_airfryer',
    name: 'Freidora de Aire Cecofry Deluxe (Cecotec)',
    icon: '🍟',
    category: 'cocina',
    durationHours: 0.35,
    powerKw: 1.40,
    totalEnergyKwh: 0.45,
    realPowerW: 1400,
    description: 'Freidora de aire caliente Cecotec Cecofry Deluxe 2.5L para almuerzos/cenas rápidas.'
  },
  {
    id: 'grunkel_toaster',
    name: 'Tostador Plano Grunkel (Desayuno)',
    icon: '🍞',
    category: 'cocina',
    durationHours: 0.15,
    powerKw: 0.65,
    totalEnergyKwh: 0.08,
    realPowerW: 650,
    description: 'Tostador horizontal plano Grunkel con barras de cuarzo para desayunos de 08:00 a 09:00 h.'
  },
  {
    id: 'coffee_maker',
    name: 'Cafetera Express (Desayuno / Sobremesa)',
    icon: '☕',
    category: 'cocina',
    durationHours: 0.1,
    powerKw: 1.20,
    totalEnergyKwh: 0.06,
    realPowerW: 1200,
    description: 'Cafetera eléctrica con calentamiento rápido de agua para desayuno matinal.'
  },
  {
    id: 'digital_microwave',
    name: 'Microondas Digital con Grill',
    icon: '🍲',
    category: 'cocina',
    durationHours: 0.2,
    powerKw: 1.20,
    totalEnergyKwh: 0.24,
    realPowerW: 1200,
    description: 'Microondas digital con display frontal y selector de potencia.'
  },
  {
    id: 'wife_study_station',
    name: 'Puesto de Estudio Mujer (Habitación Indep.)',
    icon: '📚👩‍💻',
    category: 'trabajo',
    durationHours: 6.0,
    powerKw: 0.11,
    totalEnergyKwh: 0.66,
    realPowerW: 110,
    description: 'Ordenador propio + pantalla + iluminación en habitación independiente para estudios diurnos.'
  },
  {
    id: 'user_study_overtime',
    name: 'Estudios Usuario + Monitor + Ventilador',
    icon: '💻📖',
    category: 'trabajo',
    durationHours: 4.0,
    powerKw: 0.14,
    totalEnergyKwh: 0.56,
    realPowerW: 140,
    description: 'Portátil secundario de estudios + monitor externo + ventilador en horario de tarde/noche.'
  },
  {
    id: 'superser_dryer',
    name: 'Secadora Superser SRV-200 Natural Dry',
    icon: '👔',
    category: 'lavado',
    durationHours: 1.25,
    powerKw: 2.20,
    totalEnergyKwh: 2.50,
    realPowerW: 2200,
    description: 'Secadora de evacuación Superser SRV-200 para ropa delicada o días nublados.'
  },
  {
    id: 'telework_laptops',
    name: '2x Portátiles Teletrabajo + Monitor',
    icon: '💻',
    category: 'trabajo',
    durationHours: 8.0,
    powerKw: 0.13,
    totalEnergyKwh: 1.04,
    realPowerW: 130,
    description: '2 laptops de desarrollo/teletrabajo con pantallas externas encendidas en horario laboral.'
  },
  {
    id: 'living_tv',
    name: 'Televisor Smart TV Salón',
    icon: '📺',
    category: 'ocio',
    durationHours: 4.0,
    powerKw: 0.085,
    totalEnergyKwh: 0.34,
    realPowerW: 85,
    description: 'Smart TV salón en horas de sobremesa y noche.'
  },
  {
    id: 'taurus_fan',
    name: 'Ventilador de Pie Taurus',
    icon: '💨',
    category: 'clima',
    durationHours: 6.0,
    powerKw: 0.045,
    totalEnergyKwh: 0.27,
    realPowerW: 45,
    description: 'Ventilador oscilante Taurus para circulación de brisa en el hogar.'
  },
  {
    id: 'home_lights_wifi',
    name: 'Router WiFi + Luces LED Hogar',
    icon: '💡',
    category: 'base',
    durationHours: 24.0,
    powerKw: 0.075,
    totalEnergyKwh: 0.95,
    realPowerW: 75,
    description: 'Router de fibra óptica permanente (15W) + Iluminación LED de bajo consumo (60W).'
  },
  {
    id: 'solar_thermal_acs',
    name: 'Agua Caliente Sanitaria (Placas Solares Térmicas)',
    icon: '☀️🚿',
    category: 'solar',
    durationHours: 24.0,
    powerKw: 0.0,
    totalEnergyKwh: 0.0,
    realPowerW: 0,
    description: 'Placas solares térmicas independientes dedicadas exclusivamente a calentar el agua. Consumo de red = 0.00 W.'
  },
  {
    id: 'omoda7_ev_charge',
    name: 'Recarga Omoda 7 SHS (18.7 kWh PHEV)',
    icon: '🚗⚡',
    category: 'movilidad',
    durationHours: 3.5,
    powerKw: 2.30,
    totalEnergyKwh: 8.05,
    realPowerW: 2300,
    description: 'Carga inteligente Wallbox modulando con el excedente solar directo hacia la batería del coche.'
  }
];

export class ApplianceRecommender {
  constructor(solarEngine) {
    this.engine = solarEngine;
  }

  /**
   * Evalúa todos los electrodomésticos para un día específico y devuelve la mejor franja horaria para cada uno
   * @param {Array} hourlyPoints - 24 horas del día con producción solar, carga de batería y consumo hogar
   * @returns {Array} Lista ordenada de recomendaciones horarias con coste y % solar
   */
  getRecommendationsForDay(hourlyPoints) {
    return APPLIANCE_CATALOG.map(appliance => {
      const bestWindow = this.findBestWindowForAppliance(appliance, hourlyPoints);
      return {
        ...appliance,
        bestWindow
      };
    });
  }

  /**
   * Encuentra la ventana horaria de duración D que maximiza el autoconsumo solar y minimiza la importación de red
   */
  findBestWindowForAppliance(appliance, hourlyPoints) {
    const duration = Math.ceil(appliance.durationHours);
    let bestStartHour = 12;
    let maxSolarCoverageRatio = -1;
    let minCostEur = 999;
    let bestSurplusKw = 0;

    for (let startH = 6; startH <= 23 - duration; startH++) {
      let solarCoveredKwh = 0;
      let gridImportKwh = 0;
      let windowSurplusSum = 0;

      for (let h = 0; h < duration; h++) {
        const point = hourlyPoints[startH + h];
        if (!point) continue;

        const solarGenKw = point.forecast.pTotalAC_kW;
        const homeLoadKw = (point.battery ? point.battery.homeLoadW : 700) / 1000;
        const batterySoc = point.battery ? point.battery.socPercent : 50;

        // Excedente disponible directo
        const availableDirectSurplus = Math.max(0, solarGenKw - homeLoadKw);
        // Respaldo de batería de casa si está por encima del 50%
        const batteryBackupKw = batterySoc > 40 ? 1.5 : 0;
        const totalAvailableCleanKw = availableDirectSurplus + batteryBackupKw;

        const applianceKwInHour = appliance.powerKw;

        if (totalAvailableCleanKw >= applianceKwInHour) {
          solarCoveredKwh += (appliance.totalEnergyKwh / duration);
        } else {
          const coveredPart = Math.max(0, totalAvailableCleanKw);
          const deficit = Math.max(0, applianceKwInHour - coveredPart);
          solarCoveredKwh += (coveredPart / applianceKwInHour) * (appliance.totalEnergyKwh / duration);
          gridImportKwh += (deficit / applianceKwInHour) * (appliance.totalEnergyKwh / duration);
        }

        windowSurplusSum += availableDirectSurplus;
      }

      const coverageRatio = solarCoveredKwh / appliance.totalEnergyKwh;
      const costEur = gridImportKwh * 0.12;

      // Criterio de selección: mayor % solar, y en empate mayor excedente libre
      if (coverageRatio > maxSolarCoverageRatio || (coverageRatio === maxSolarCoverageRatio && costEur < minCostEur)) {
        maxSolarCoverageRatio = coverageRatio;
        minCostEur = costEur;
        bestStartHour = startH;
        bestSurplusKw = windowSurplusSum / duration;
      }
    }

    const endHour = bestStartHour + appliance.durationHours;
    const formatTime = (h) => {
      const hours = Math.floor(h);
      const mins = Math.round((h - hours) * 60);
      return `${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}`;
    };

    const is100PercentFree = maxSolarCoverageRatio >= 0.95;

    return {
      startHour: bestStartHour,
      endHour,
      timeRangeStr: `${formatTime(bestStartHour)} - ${formatTime(endHour)} h`,
      solarCoveragePercent: Math.round(maxSolarCoverageRatio * 100),
      is100PercentFree,
      costEur: minCostEur,
      costWithoutSolarEur: appliance.totalEnergyKwh * 0.12,
      savingsEur: Math.max(0, (appliance.totalEnergyKwh * 0.12) - minCostEur),
      surplusAvailableKw: bestSurplusKw
    };
  }

  /**
   * Calcula el plan de carga personalizado para el Omoda 7 SHS
   * @param {Object} params - { startHour, endHour, currentSocPercent, targetSocPercent, maxKw }
   * @param {Array} hourlyPoints - 24 horas del día seleccionado
   */
  calculateCustomEvChargePlan(params, hourlyPoints) {
    const startH = parseInt(params.startHour || 13);
    const endH = parseInt(params.endHour || 18);
    const currentSoc = parseFloat(params.currentSocPercent || 20);
    const targetSoc = parseFloat(params.targetSocPercent || 90);
    const maxChargerKw = parseFloat(params.maxKw || 3.7);

    const totalBatteryKwh = 18.7;
    const neededKwh = Math.max(0, ((targetSoc - currentSoc) / 100) * totalBatteryKwh);

    let deliveredKwh = 0;
    let solarDeliveredKwh = 0;
    let gridDeliveredKwh = 0;
    const hourlySchedule = [];

    for (let h = 0; h < 24; h++) {
      const isConnected = (startH <= endH) 
        ? (h >= startH && h < endH)
        : (h >= startH || h < endH); // Caso nocturno cruzando medianoche

      let chargeKw = 0;
      let isSolar = false;

      if (isConnected && deliveredKwh < neededKwh) {
        const point = hourlyPoints[h];
        const solarGenKw = point ? point.forecast.pTotalAC_kW : 0;
        const homeLoadKw = (point && point.battery ? point.battery.homeLoadW : 700) / 1000;
        const homeBatterySoc = point && point.battery ? point.battery.socPercent : 50;

        const availableSolarSurplus = Math.max(0, solarGenKw - homeLoadKw);
        const remainingToDeliver = neededKwh - deliveredKwh;

        if (params.mode === 'solar_only') {
          // Solo si hay excedente real o batería de casa llena
          if (availableSolarSurplus >= 1.38 || homeBatterySoc >= 95) {
            chargeKw = Math.min(maxChargerKw, availableSolarSurplus, remainingToDeliver);
            isSolar = true;
            solarDeliveredKwh += chargeKw;
            deliveredKwh += chargeKw;
          }
        } else if (params.mode === 'fast_hybrid') {
          // Carga a máxima potencia usando todo el sol disponible y completando de red
          chargeKw = Math.min(maxChargerKw, remainingToDeliver);
          const solarPart = Math.min(chargeKw, availableSolarSurplus);
          const gridPart = chargeKw - solarPart;

          solarDeliveredKwh += solarPart;
          gridDeliveredKwh += gridPart;
          deliveredKwh += chargeKw;
          isSolar = solarPart > (gridPart);
        } else {
          // Modo Nocturno / Valle
          chargeKw = Math.min(maxChargerKw, remainingToDeliver);
          gridDeliveredKwh += chargeKw;
          deliveredKwh += chargeKw;
        }
      }

      hourlySchedule.push({
        hour: h,
        isConnected,
        chargeKw,
        isSolar,
        deliveredKwhSoFar: deliveredKwh,
        currentVehicleSocPercent: Math.min(100, currentSoc + (deliveredKwh / totalBatteryKwh) * 100)
      });
    }

    const electricCostEur = gridDeliveredKwh * 0.12;
    const equivalentKm = (deliveredKwh / totalBatteryKwh) * 95;
    const gasolineCostSavedEur = (equivalentKm / 100) * 7.5 * 1.60;

    return {
      neededKwh,
      deliveredKwh,
      solarDeliveredKwh,
      gridDeliveredKwh,
      solarPercent: deliveredKwh > 0 ? Math.round((solarDeliveredKwh / deliveredKwh) * 100) : 100,
      equivalentKmAdded: Math.round(equivalentKm),
      electricCostEur,
      gasolineCostSavedEur,
      netProfitEur: gasolineCostSavedEur - electricCostEur,
      hourlySchedule
    };
  }
}
