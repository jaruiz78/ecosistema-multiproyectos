/**
 * Planificador de Movilidad Inteligente para el Omoda 7 SHS
 * Derivado de AppViajes y ProyectoLogistica.
 * Modela consumos de trayectos habituales desde Tocina y planifica la recarga solar 100% gratuita.
 */

export const PRESET_ROUTES = [
  {
    id: 'sevilla_centro',
    name: 'Tocina ⇄ Sevilla Centro (A-4 / A-8005)',
    distanceKm: 84.0, // Ida y vuelta
    avgSpeedKmh: 75.0,
    baseConsumptionKwhPer100Km: 18.5,
    description: 'Trayecto laboral diario por carretera comarcal y autovía.'
  },
  {
    id: 'aeropuerto_poligonos',
    name: 'Tocina ⇄ Aeropuerto / Polígonos Sevilla',
    distanceKm: 70.0, // Ida y vuelta
    avgSpeedKmh: 80.0,
    baseConsumptionKwhPer100Km: 17.8,
    description: 'Ruta fluida por A-4.'
  },
  {
    id: 'carmona_lora',
    name: 'Tocina ⇄ Carmona / Lora del Río',
    distanceKm: 46.0, // Ida y vuelta
    avgSpeedKmh: 65.0,
    baseConsumptionKwhPer100Km: 16.5,
    description: 'Desplazamiento interurbano comarcal.'
  },
  {
    id: 'local_tocina',
    name: 'Urbano Tocina - Los Rosales',
    distanceKm: 15.0,
    avgSpeedKmh: 35.0,
    baseConsumptionKwhPer100Km: 15.0,
    description: 'Recados urbanos y colegios en el municipio.'
  }
];

export class MobilityPlanner {
  constructor() {
    this.gasolinePriceEurLitre = 1.62;
    this.omodaThermalLitrePer100Km = 7.4; // Consumo real del Omoda 7 en modo solo gasolina
    this.batteryCapacityKwh = 18.7;
    this.maxPureElectricRangeKm = 95.0;
  }

  calculateRouteEnergyNeed(route, ambientTempC = 30) {
    // Factor de climatización (A/C en verano de Sevilla >35°C añade ~12% de consumo)
    const hvacFactor = ambientTempC > 32 ? 1.12 : (ambientTempC < 10 ? 1.15 : 1.0);
    const totalKwhNeeded = (route.distanceKm / 100.0) * route.baseConsumptionKwhPer100Km * hvacFactor;

    // ¿Cubre la batería el 100% de la ruta?
    const electricCoveragePercent = Math.min(100, Math.round((this.maxPureElectricRangeKm / route.distanceKm) * 100));
    const electricKm = Math.min(route.distanceKm, this.maxPureElectricRangeKm);
    const gasolineKm = Math.max(0, route.distanceKm - electricKm);

    // Coste si se hiciera en gasolina vs Solar 100% gratis
    const gasolineCostEur = (route.distanceKm / 100.0) * this.omodaThermalLitrePer100Km * this.gasolinePriceEurLitre;
    const solarCostEur = 0.00; // Recargado con excedente fotovoltaico
    const savingsPerTripEur = gasolineCostEur - solarCostEur;

    return {
      routeId: route.id,
      routeName: route.name,
      distanceKm: route.distanceKm,
      kwhNeeded: Math.min(this.batteryCapacityKwh, totalKwhNeeded),
      electricKm,
      gasolineKm,
      electricCoveragePercent,
      gasolineCostEur: Math.round(gasolineCostEur * 100) / 100,
      solarCostEur: 0.00,
      savingsPerTripEur: Math.round(savingsPerTripEur * 100) / 100,
      monthlySavingsEur: Math.round(savingsPerTripEur * 22 * 100) / 100 // 22 días laborables
    };
  }
}
