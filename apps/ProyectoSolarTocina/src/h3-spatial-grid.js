/**
 * Motor Geoespacial Uber H3 y Microclima del Valle del Guadalquivir
 * Derivado de core-geogrid-h3 para modelar nieblas, albedo y calima estacional en Tocina.
 */

export class H3SpatialMicroclimate {
  constructor(lat = 37.5942, lon = -5.7397) {
    this.lat = lat;
    this.lon = lon;
    // Celdas H3 estimadas para Tocina / Los Rosales (Valle Medio del Guadalquivir)
    this.h3Indexes = {
      res7: '873902344ffffff', // Macro-zona Vega del Guadalquivir (~5 km radio)
      res8: '8839023447fffff', // Término Municipal Tocina-Los Rosales (~1.2 km radio)
      res9: '8939023447bffff'  // Micro-celda Amadeo Vives (~400 m radio)
    };
  }

  getMicroclimateProfile(month = new Date().getMonth() + 1, currentTemp = 32) {
    // 1. Efecto Calima / Polvo Sahariano en verano (Julio-Agosto en Sevilla)
    const isSummerSaharaSeason = month >= 6 && month <= 8;
    const aerosolOpticalDepth = isSummerSaharaSeason ? 0.22 : 0.08;
    const diffuseRatioBonus = isSummerSaharaSeason ? 0.08 : 0.02;

    // 2. Inversión Térmica y Niebla de la Vega en Invierno (Diciembre-Febrero)
    const isWinterFogSeason = month >= 11 || month <= 2;
    const morningFogRisk = isWinterFogSeason ? 0.35 : 0.05;

    // 3. Albedo del entorno (cubiertas urbanas de teja cerámica y grava en Los Rosales)
    const urbanAlbedo = 0.20;

    return {
      h3CellRes9: this.h3Indexes.res9,
      h3CellRes8: this.h3Indexes.res8,
      regionName: 'Vega del Guadalquivir (Baja Altitud 31m)',
      aerosolOpticalDepth,
      diffuseRatioBonus,
      morningFogRisk,
      urbanAlbedo,
      thermalInversionFactor: currentTemp > 38 ? 1.05 : 1.0,
      description: isSummerSaharaSeason
        ? 'Verano Andorrano/Sevillano: Alta radiación directa con ligera dispersión por calima sahariana.'
        : (isWinterFogSeason ? 'Invierno en la Vega: Posibilidad de nieblas matinales de irradiación (08:00 - 10:30 h).' : 'Primavera/Otoño: Máxima claridad atmosférica y óptima ventilación.')
    };
  }
}
