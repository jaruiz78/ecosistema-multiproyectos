/**
 * Centro Meteorológico y Radar Satelital en Tiempo Real (MultiProyectos AI)
 * - Mapas Doppler de precipitación y satélite infrarrojo EUMETSAT vía Leaflet.js
 * - Estación meteorológica completa en vivo (temperatura, sensación, rocío, viento, presión, UV, capas de nubes)
 * - Pronóstico y radiación solar a 7 días con desglose multi-variable
 */

class RadarWeatherCenter {
  constructor() {
    this.map = null;
    this.radarLayer = null;
    this.homeMarker = null;
    this.radarData = null;
    this.currentFrameIndex = 0;
    this.isPlaying = false;
    this.playInterval = null;
    this.activeLayerType = 'radar'; // 'radar' | 'satellite'
    this.lat = 37.5942;
    this.lon = -5.7397;
  }

  async init() {
    this.initMap();
    await this.fetchCurrentWeather();
    await this.fetchRadarLayers();
    this.setupEventListeners();
    
    // Auto-refresco de estación cada 60 segundos y radar cada 5 minutos
    setInterval(() => this.fetchCurrentWeather(), 60000);
    setInterval(() => this.fetchRadarLayers(), 300000);
  }

  initMap() {
    const mapContainer = document.getElementById('radar-map-container');
    if (!mapContainer || !window.L) return;

    if (this.map) {
      this.map.remove();
      this.map = null;
    }

    // Inicializar mapa Leaflet centrado en Tocina / Los Rosales / Sevilla (Zoom 7 para máxima resolución de radar sin watermark)
    this.map = L.map('radar-map-container', {
      center: [this.lat, this.lon],
      zoom: 7,
      minZoom: 5,
      maxZoom: 11,
      zoomControl: false,
      attributionControl: false
    });

    // Control de zoom en esquina superior derecha
    L.control.zoom({ position: 'topright' }).addTo(this.map);

    // Capa base oscura ultra-limpia (Esri Dark Gray Canvas)
    L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Dark_Gray_Base/MapServer/tile/{z}/{y}/{x}', {
      maxZoom: 16
    }).addTo(this.map);

    // Marcador luminoso en la posición exacta de la vivienda (C/ Amadeo Vives 31)
    const customIcon = L.divIcon({
      className: 'radar-home-marker',
      html: `
        <div style="position: relative; display: flex; align-items: center; justify-content: center; width: 32px; height: 32px;">
          <div style="position: absolute; width: 28px; height: 28px; border-radius: 50%; background: rgba(56, 189, 248, 0.4); animation: pulse 2s infinite;"></div>
          <div style="position: absolute; width: 14px; height: 14px; border-radius: 50%; background: #38bdf8; border: 2px solid #ffffff; box-shadow: 0 0 10px #38bdf8;"></div>
        </div>
      `,
      iconSize: [32, 32],
      iconAnchor: [16, 16]
    });

    this.homeMarker = L.marker([this.lat, this.lon], { icon: customIcon }).addTo(this.map);
    this.homeMarker.bindPopup(`
      <div style="color: #0f172a; font-family: sans-serif; font-size: 0.85rem; padding: 4px;">
        <strong>☀️ Planta Solar Tocina</strong><br>
        C/ Amadeo Vives 31, Los Rosales<br>
        <span style="font-size: 0.75rem; color: #64748b;">37.594° N, -5.740° O | 5.00 kWp</span>
      </div>
    `);
  }

  async fetchCurrentWeather() {
    try {
      const resp = await fetch('/api/weather/current');
      if (!resp.ok) return;
      const data = await resp.json();
      this.renderCurrentWeather(data);
    } catch (e) {
      console.warn('[RadarWeather] Error fetching current weather:', e);
    }
  }

  renderCurrentWeather(data) {
    // 1. Badge y Estado Principal
    const tempEl = document.getElementById('meteo-live-temp');
    if (tempEl) tempEl.textContent = `${data.temperature_c.toFixed(1)}°C`;

    const appTempEl = document.getElementById('meteo-live-apparent');
    if (appTempEl) appTempEl.textContent = `Sensación ${data.apparent_temperature_c.toFixed(1)}°C`;

    const iconEl = document.getElementById('meteo-live-icon');
    if (iconEl) iconEl.textContent = data.weather_icon || '🌤️';

    const descEl = document.getElementById('meteo-live-desc');
    if (descEl) descEl.textContent = data.weather_desc || 'Parcialmente Nublado';

    // 2. Humedad y Punto de Rocío
    const humEl = document.getElementById('meteo-live-humidity');
    if (humEl) humEl.textContent = `${data.humidity_percent}%`;

    const dewEl = document.getElementById('meteo-live-dew');
    if (dewEl) dewEl.textContent = `Pto. Rocío: ${data.dew_point_c}°C`;

    // 3. Viento y Rachas
    const windSpeedEl = document.getElementById('meteo-live-wind-speed');
    if (windSpeedEl) windSpeedEl.textContent = `${data.wind_speed_kmh} km/h`;

    const windDirEl = document.getElementById('meteo-live-wind-dir');
    if (windDirEl) windDirEl.textContent = `${data.wind_cardinal} (${data.wind_direction_deg}°) · Rachas: ${data.wind_gusts_kmh} km/h`;

    // 4. Presión Atmosférica
    const pressEl = document.getElementById('meteo-live-pressure');
    if (pressEl) pressEl.textContent = `${data.pressure_hpa} hPa`;

    // 5. Radiación y UV Index
    const uvEl = document.getElementById('meteo-live-uv');
    if (uvEl) uvEl.textContent = `${data.uv_index}`;

    const uvMaxEl = document.getElementById('meteo-live-uv-max');
    if (uvMaxEl) uvMaxEl.textContent = `Pico máx hoy: ${data.uv_max_today} UV`;

    // 6. Cobertura Nubosa por Capas
    const cloudEl = document.getElementById('meteo-live-cloud-total');
    if (cloudEl) cloudEl.textContent = `${data.cloud_cover_percent}%`;

    const cloudLayersEl = document.getElementById('meteo-live-cloud-layers');
    if (cloudLayersEl && data.cloud_layers) {
      cloudLayersEl.textContent = `Bajas: ${data.cloud_layers.low}% · Medias: ${data.cloud_layers.mid}% · Altas: ${data.cloud_layers.high}%`;
    }

    // 7. Sol y Fotoperiodo
    const sunTimesEl = document.getElementById('meteo-live-sun-times');
    if (sunTimesEl && data.sun) {
      sunTimesEl.textContent = `🌅 ${data.sun.sunrise} h  |  🌇 ${data.sun.sunset} h  (${data.sun.daylight_duration_hours} h luz)`;
    }

    // 8. Visibilidad y lluvia hoy
    const rainTodayEl = document.getElementById('meteo-live-rain-today');
    if (rainTodayEl && data.today_stats) {
      rainTodayEl.textContent = `${data.today_stats.rain_mm} mm (Prob. máx: ${data.today_stats.precip_prob_max}%)`;
    }
  }

  async fetchRadarLayers() {
    try {
      const resp = await fetch('/api/weather/radar-layers');
      if (!resp.ok) return;
      this.radarData = await resp.json();
      this.initRadarTimeline();
    } catch (e) {
      console.warn('[RadarWeather] Error fetching radar layers:', e);
    }
  }

  initRadarTimeline() {
    if (!this.radarData || !this.map) return;

    const frames = (this.activeLayerType === 'satellite') 
      ? (this.radarData.satellite_frames || [])
      : (this.radarData.radar_frames || []);

    const slider = document.getElementById('radar-time-slider');
    const timeLabel = document.getElementById('radar-frame-time-lbl');
    const liveBadge = document.getElementById('radar-live-badge');

    if (frames.length === 0) {
      if (this.activeLayerType === 'satellite') {
        if (this.radarLayer) this.map.removeLayer(this.radarLayer);
        const yesterday = new Date(Date.now() - 86400000).toISOString().split('T')[0];
        const gibsUrl = `https://gibs.earthdata.nasa.gov/wmts/epsg3857/best/VIIRS_SNPP_CorrectedReflectance_TrueColor/default/${yesterday}/GoogleMapsCompatible_Level9/{z}/{y}/{x}.jpg`;
        this.radarLayer = L.tileLayer(gibsUrl, {
          maxNativeZoom: 8,
          maxZoom: 11,
          opacity: 0.85,
          zIndex: 100
        }).addTo(this.map);
        if (timeLabel) timeLabel.textContent = `🛰️ Satélite EUMETSAT / NASA Terra-Aqua (HD Óptico & Nubosidad)`;
        if (liveBadge) {
          liveBadge.textContent = '🛰️ SATÉLITE HD';
          liveBadge.style.color = '#38bdf8';
        }
      } else {
        if (timeLabel) timeLabel.textContent = 'Sin cobertura de radar en vivo';
      }
      return;
    }

    if (slider) {
      slider.max = frames.length - 1;
      slider.value = frames.length - 1;
    }

    this.currentFrameIndex = frames.length - 1;
    this.showRadarFrame(this.currentFrameIndex);
  }

  showRadarFrame(index) {
    if (!this.radarData || !this.map) return;

    const frames = (this.activeLayerType === 'satellite') 
      ? (this.radarData.satellite_frames || [])
      : (this.radarData.radar_frames || []);

    if (index < 0 || index >= frames.length) return;

    const frame = frames[index];
    const host = this.radarData.host || 'https://tilecache.rainviewer.com';
    const tileUrl = `${host}${frame.path}/256/{z}/{x}/{y}/2/1_1.png`;

    if (this.radarLayer) {
      this.map.removeLayer(this.radarLayer);
    }

    this.radarLayer = L.tileLayer(tileUrl, {
      tileSize: 256,
      maxNativeZoom: 7,
      maxZoom: 11,
      opacity: 0.80,
      zIndex: 100
    }).addTo(this.map);

    // Formatear hora del frame (timestamp epoch)
    const frameDate = new Date(frame.time * 1000);
    const hours = frameDate.getHours().toString().padStart(2, '0');
    const mins = frameDate.getMinutes().toString().padStart(2, '0');
    const isLatest = (index === frames.length - 1);

    const timeLabel = document.getElementById('radar-frame-time-lbl');
    if (timeLabel) {
      timeLabel.textContent = `🛰️ Frame: ${hours}:${mins} h ${isLatest ? '(EN VIVO / NOWCAST)' : ''}`;
    }

    const slider = document.getElementById('radar-time-slider');
    if (slider && parseInt(slider.value, 10) !== index) {
      slider.value = index;
    }

    const liveBadge = document.getElementById('radar-live-badge');
    if (liveBadge) {
      liveBadge.textContent = isLatest ? '🔴 EN VIVO' : '⏱️ HISTÓRICO';
      liveBadge.style.color = isLatest ? '#10b981' : '#fbbf24';
    }
  }

  togglePlay() {
    this.isPlaying = !this.isPlaying;
    const playBtn = document.getElementById('radar-play-btn');
    if (playBtn) playBtn.textContent = this.isPlaying ? '⏸️ Pausar' : '▶️ Reproducir';

    if (this.isPlaying) {
      this.playInterval = setInterval(() => {
        const frames = (this.activeLayerType === 'satellite') 
          ? (this.radarData.satellite_frames || [])
          : (this.radarData.radar_frames || []);
        
        if (frames.length === 0) return;
        this.currentFrameIndex = (this.currentFrameIndex + 1) % frames.length;
        this.showRadarFrame(this.currentFrameIndex);
      }, 600);
    } else {
      if (this.playInterval) clearInterval(this.playInterval);
    }
  }

  setLayerType(type) {
    this.activeLayerType = type;
    const btnRadar = document.getElementById('btn-layer-radar');
    const btnSat = document.getElementById('btn-layer-satellite');

    if (btnRadar && btnSat) {
      if (type === 'radar') {
        btnRadar.classList.add('active');
        btnSat.classList.remove('active');
      } else {
        btnSat.classList.add('active');
        btnRadar.classList.remove('active');
      }
    }

    this.initRadarTimeline();
  }

  setupEventListeners() {
    const playBtn = document.getElementById('radar-play-btn');
    if (playBtn) {
      playBtn.addEventListener('click', () => this.togglePlay());
    }

    const slider = document.getElementById('radar-time-slider');
    if (slider) {
      slider.addEventListener('input', (e) => {
        if (this.isPlaying) this.togglePlay();
        this.currentFrameIndex = parseInt(e.target.value, 10);
        this.showRadarFrame(this.currentFrameIndex);
      });
    }

    const btnRadar = document.getElementById('btn-layer-radar');
    if (btnRadar) {
      btnRadar.addEventListener('click', () => this.setLayerType('radar'));
    }

    const btnSat = document.getElementById('btn-layer-satellite');
    if (btnSat) {
      btnSat.addEventListener('click', () => this.setLayerType('satellite'));
    }

    const centerBtn = document.getElementById('radar-center-btn');
    if (centerBtn) {
      centerBtn.addEventListener('click', () => {
        if (this.map) this.map.setView([this.lat, this.lon], 9, { animate: true });
      });
    }
  }
}

window.radarWeatherCenter = new RadarWeatherCenter();
