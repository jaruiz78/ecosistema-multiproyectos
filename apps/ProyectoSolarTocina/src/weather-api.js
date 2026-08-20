/**
 * Weather API Client para Open-Meteo Solar Forecast
 * Coordenadas: 37.5942 N, -5.7397 W (Los Rosales - Tocina, Sevilla)
 */

export class WeatherApiClient {
  constructor(lat = 37.5942, lon = -5.7397) {
    this.lat = lat;
    this.lon = lon;
    this.localBrokerUrl = '/api/weather/forecast';
    this.directBaseUrl = 'https://api.open-meteo.com/v1/forecast';
  }

  /**
   * Obtiene la previsión horaria para 7 a 14 días a través del Weather Broker local (O(1))
   */
  async fetchHourlyForecast(days = 7, forceRefresh = false) {
    // 1. Intentar primero a través del Weather Broker Centralizado (Caché SQLite local sin latencia)
    try {
      const localUrl = `${this.localBrokerUrl}?lat=${this.lat}&lon=${this.lon}&days=${days}&refresh=${forceRefresh ? '1' : '0'}`;
      const localResp = await fetch(localUrl);
      if (localResp.ok) {
        const data = await localResp.json();
        if (data && data.hourly) {
          return this.parseHourlyData(data);
        }
      }
    } catch (e) {
      console.warn('Weather Broker local no disponible, intentando acceso directo a Open-Meteo:', e);
    }

    // 2. Fallback de acceso directo a Open-Meteo API
    const params = new URLSearchParams({
      latitude: this.lat.toString(),
      longitude: this.lon.toString(),
      hourly: [
        'temperature_2m',
        'relative_humidity_2m',
        'apparent_temperature',
        'precipitation_probability',
        'weather_code',
        'cloud_cover',
        'direct_normal_irradiance_instant',
        'diffuse_radiation_instant',
        'shortwave_radiation_instant',
        'direct_radiation_instant',
        'is_day',
        'sunshine_duration'
      ].join(','),
      daily: [
        'sunrise',
        'sunset',
        'uv_index_max',
        'temperature_2m_max',
        'temperature_2m_min',
        'precipitation_probability_max'
      ].join(','),
      timezone: 'Europe/Madrid',
      forecast_days: days.toString()
    });

    const url = `${this.directBaseUrl}?${params.toString()}`;

    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`Error en API Open-Meteo: ${response.status} ${response.statusText}`);
      }
      const data = await response.json();
      return this.parseHourlyData(data);
    } catch (err) {
      console.warn('Fallo en la llamada a Open-Meteo, generando datos sintéticos climatológicos de Sevilla:', err);
      return this.generateClimatologicalFallback(days);
    }
  }

  parseHourlyData(data) {
    const h = data.hourly;
    const count = h.time.length;
    const series = [];

    for (let i = 0; i < count; i++) {
      const date = new Date(h.time[i]);
      series.push({
        time: h.time[i],
        date,
        temp: h.temperature_2m[i],
        apparentTemp: h.apparent_temperature ? h.apparent_temperature[i] : h.temperature_2m[i],
        dewPoint: h.dew_point_2m ? h.dew_point_2m[i] : 15.0,
        humidity: h.relative_humidity_2m ? h.relative_humidity_2m[i] : 45,
        cloudCover: h.cloud_cover ? h.cloud_cover[i] : 0,
        cloudLow: h.cloud_cover_low ? h.cloud_cover_low[i] : 0,
        cloudMid: h.cloud_cover_mid ? h.cloud_cover_mid[i] : 0,
        cloudHigh: h.cloud_cover_high ? h.cloud_cover_high[i] : 0,
        precipProb: h.precipitation_probability ? h.precipitation_probability[i] : 0,
        precipitation: h.precipitation ? h.precipitation[i] : 0,
        weatherCode: h.weather_code ? h.weather_code[i] : 0,
        windSpeed: h.wind_speed_10m ? h.wind_speed_10m[i] : 5.0,
        windDir: h.wind_direction_10m ? h.wind_direction_10m[i] : 0,
        windGusts: h.wind_gusts_10m ? h.wind_gusts_10m[i] : 8.0,
        uvIndex: h.uv_index ? h.uv_index[i] : 0,
        surfacePressure: h.surface_pressure ? h.surface_pressure[i] : 1015.0,
        dni: h.direct_normal_irradiance_instant ? h.direct_normal_irradiance_instant[i] : 0,
        dhi: h.diffuse_radiation_instant ? h.diffuse_radiation_instant[i] : 0,
        ghi: h.shortwave_radiation_instant ? h.shortwave_radiation_instant[i] : 0,
        isDay: h.is_day ? h.is_day[i] === 1 : date.getHours() >= 7 && date.getHours() <= 21
      });
    }

    return {
      source: 'Open-Meteo Solar API (Live)',
      cacheMeta: data._cache_meta || null,
      location: {
        name: 'Los Rosales - Tocina, Sevilla',
        address: 'Calle Amadeo Vives 31',
        lat: this.lat,
        lon: this.lon,
        elevation: data.elevation || 31
      },
      current: data.current || null,
      minutely_15: data.minutely_15 || null,
      daily: data.daily || {},
      hourly: series
    };
  }

  /**
   * Respaldo Climatológico específico para Tocina / Sevilla (Típico verano/primavera soleado con ligera variación)
   */
  generateClimatologicalFallback(days = 7) {
    const series = [];
    const now = new Date();
    const startDate = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);

    for (let d = 0; d < days; d++) {
      for (let hour = 0; hour < 24; hour++) {
        const currentDate = new Date(startDate.getTime() + (d * 24 + hour) * 3600000);
        
        // Simular temperatura de Sevilla (mínima 20-22°C a las 07:00, máxima 36-39°C a las 17:00)
        const hourFactor = Math.sin(((hour - 9) / 24) * 2 * Math.PI);
        const temp = Math.round(28 + 10 * hourFactor + (Math.random() * 2 - 1));
        
        // Nubes esporádicas en días posteriores
        const cloudCover = d === 2 ? 40 : (d === 5 ? 25 : Math.round(Math.random() * 10));
        
        // Radiación solar estimada si no hay red
        let dni = 0;
        let dhi = 0;
        let ghi = 0;
        const isDay = hour >= 7 && hour <= 21;

        if (isDay) {
          const solarElevSin = Math.sin(((hour - 7) / 14) * Math.PI);
          const clearDni = 950 * Math.pow(solarElevSin, 0.8);
          const clearGhi = 1000 * Math.pow(solarElevSin, 1.1);
          const clearDhi = 120 * solarElevSin;

          const cloudRed = Math.max(0.1, 1 - (cloudCover / 100) * 0.85);
          dni = Math.max(0, Math.round(clearDni * cloudRed));
          dhi = Math.max(0, Math.round(clearDhi * (0.5 + 0.5 * (cloudCover / 100))));
          ghi = Math.max(0, Math.round(clearGhi * cloudRed));
        }

        series.push({
          time: currentDate.toISOString().slice(0, 16),
          date: currentDate,
          temp,
          humidity: Math.round(40 - 15 * hourFactor),
          cloudCover,
          precipProb: cloudCover > 30 ? 15 : 0,
          weatherCode: cloudCover > 50 ? 3 : (cloudCover > 20 ? 1 : 0),
          dni,
          dhi,
          ghi,
          isDay
        });
      }
    }

    return {
      source: 'Climatología Modelo Sevilla (Offline Fallback)',
      location: {
        name: 'Los Rosales - Tocina, Sevilla',
        address: 'Calle Amadeo Vives 31',
        lat: this.lat,
        lon: this.lon,
        elevation: 31
      },
      daily: {},
      hourly: series
    };
  }

  getWeatherDescription(code) {
    const map = {
      0: 'Cielo completamente despejado',
      1: 'Mayormente soleado / Nubes altas',
      2: 'Parcialmente nuboso',
      3: 'Nublado',
      45: 'Niebla matinal',
      48: 'Niebla con escarcha',
      51: 'Llovizna ligera',
      61: 'Lluvia débil',
      63: 'Lluvia moderada',
      80: 'Chubascos dispersos',
      95: 'Tormenta eléctrica'
    };
    return map[code] || 'Soleado / Condiciones estables';
  }
}
