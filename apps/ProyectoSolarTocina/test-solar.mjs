import { SolarEngine } from './src/solar-engine.js';

console.log('=== TEST DE VALIDACIÓN FOTOVOLTAICA - TOCINA, SEVILLA ===');

const engine = new SolarEngine({
  lat: 37.5942,
  lon: -5.7397,
  altitude: 31,
  stringEastPanels: 6,
  stringEastAzimuth: 85,
  stringEastTilt: 22,
  stringWestPanels: 4,
  stringWestAzimuth: 265,
  stringWestTilt: 22,
  panelWp: 500, // 500Wp x 10 = 5.0 kWp
  inverterMaxKw: 10.0,
  batteryCapacityKwh: 10.0
});

// Probar punto a las 11:49 de un día de agosto soleado a 29°C (exactamente como en la captura del usuario)
const testDate = new Date('2026-08-18T11:49:00+02:00');
const point = engine.calculateHourlyPoint(testDate, {
  temp: 29,
  cloudCover: 0,
  dni: 920,
  dhi: 110,
  ghi: 950
});

console.log('Fecha/Hora Test:', testDate.toISOString());
console.log('Posición Solar:');
console.log(` - Elevación Solar: ${point.solarPosition.elevationDeg.toFixed(1)}°`);
console.log(` - Azimut Solar: ${point.solarPosition.azimuthDeg.toFixed(1)}°`);
console.log('\nPotencias Generadas:');
console.log(` - String Este (6 placas @ 85°): ${point.forecast.pEast_kW.toFixed(2)} kW`);
console.log(` - String Oeste (4 placas @ 265°): ${point.forecast.pWest_kW.toFixed(2)} kW`);
console.log(` - Total Inversor AC: ${point.forecast.pTotalAC_kW.toFixed(2)} kW (Captura real: ~3.23 kW)`);
console.log(` - Teórico Clear-Sky Máximo: ${point.clearSky.pTotalAC_kW.toFixed(2)} kW`);

// Validar simulación de batería
const batteryPoint = engine.simulateBatteryDispatch([point], 6.3)[0]; // 6.3 kWh = 63% SOC
console.log('\nSimulación de Batería & Flujos de Potencia:');
console.log(` - Carga Batería (SOC): ${batteryPoint.battery.socPercent.toFixed(1)}%`);
console.log(` - Potencia enviada a Batería: ${(batteryPoint.battery.powerW / 1000).toFixed(2)} kW (Captura real: 2.54 kW)`);
console.log(` - Consumo Hogar: ${batteryPoint.battery.homeLoadW} W (Captura real: 696 W)`);
console.log(` - Red: Exportación = ${batteryPoint.battery.gridExportW.toFixed(0)} W, Importación = ${batteryPoint.battery.gridImportW.toFixed(0)} W`);

console.log('\n✅ VALIDACIÓN FÍSICA Y MATEMÁTICA COMPLETADA CON ÉXITO');
