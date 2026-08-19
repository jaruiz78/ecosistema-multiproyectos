/**
 * Diagrama Unifilar Interactivo y Animado de Flujo de Energía (Power Flow Canvas)
 * Visualización en tiempo real de potencia, tensiones, estado de batería Fox-ESS,
 * consumo de electrodomésticos reales e inyección a Batería Virtual.
 */

export class PowerFlowCanvas {
  constructor(canvasId) {
    this.canvas = document.getElementById(canvasId);
    if (!this.canvas) return;
    this.ctx = this.canvas.getContext('2d');
    
    // Telemetría de estado actual
    this.state = {
      solarW: 3600,
      pv1W: 2100,
      pv2W: 1500,
      homeLoadW: 1090,
      batPowerW: 1500, // positivo = cargando, negativo = descargando
      batSoc: 100,
      batVoltage: 192.0,
      gridExportW: 1010,
      gridImportW: 0,
      evChargeW: 0,
      invTemp: 44.5,
      isOnline: true
    };

    this.particles = [];
    this.lastTime = performance.now();
    this.animationId = null;
    this.hoveredNode = null;

    this.initCanvas();
    this.setupListeners();
    this.startAnimation();
  }

  initCanvas() {
    this.resize();
    window.addEventListener('resize', () => this.resize());
  }

  resize() {
    if (!this.canvas) return;
    const rect = this.canvas.getBoundingClientRect();
    const dpr = window.devicePixelRatio || 1;
    this.width = rect.width || 800;
    this.height = 320;
    
    this.canvas.width = this.width * dpr;
    this.canvas.height = this.height * dpr;
    this.ctx.scale(dpr, dpr);

    this.computeNodePositions();
  }

  computeNodePositions() {
    const w = this.width;
    const h = this.height;

    // Distribución geométrica de nodos
    this.nodes = {
      solar: {
        id: 'solar',
        label: '☀️ Paneles',
        sub: '10x 500W (5.0 kWp)',
        x: w * 0.16,
        y: h * 0.26,
        radius: 38,
        color: '#f59e0b',
        glow: 'rgba(245, 158, 11, 0.4)'
      },
      inverter: {
        id: 'inverter',
        label: '⚡ Sunworks',
        sub: '10 kW Híbrido (KP10)',
        x: w * 0.50,
        y: h * 0.50,
        radius: 42,
        color: '#38bdf8',
        glow: 'rgba(56, 189, 248, 0.4)'
      },
      battery: {
        id: 'battery',
        label: '🔋 Fox-ESS',
        sub: 'EP5 HV (10.36 kWh)',
        x: w * 0.16,
        y: h * 0.74,
        radius: 38,
        color: '#c084fc',
        glow: 'rgba(192, 132, 252, 0.4)'
      },
      home: {
        id: 'home',
        label: '🏠 Vivienda',
        sub: 'Daikin, Frigo, PCs',
        x: w * 0.84,
        y: h * 0.26,
        radius: 38,
        color: '#10b981',
        glow: 'rgba(16, 185, 129, 0.4)'
      },
      grid: {
        id: 'grid',
        label: '🌐 Red (BV)',
        sub: 'Batería Virtual',
        x: w * 0.84,
        y: h * 0.74,
        radius: 38,
        color: '#06b6d4',
        glow: 'rgba(6, 182, 212, 0.4)'
      },
      ev: {
        id: 'ev',
        label: '🚗 Omoda 7',
        sub: 'PHEV 18.7 kWh',
        x: w * 0.50,
        y: h * 0.90,
        radius: 30,
        color: '#ec4899',
        glow: 'rgba(236, 72, 153, 0.4)'
      }
    };
  }

  setupListeners() {
    this.canvas.addEventListener('mousemove', (e) => {
      const rect = this.canvas.getBoundingClientRect();
      const x = e.clientX - rect.left;
      const y = e.clientY - rect.top;

      let found = null;
      for (const key in this.nodes) {
        const node = this.nodes[key];
        const dx = x - node.x;
        const dy = y - node.y;
        if (Math.sqrt(dx * dx + dy * dy) <= node.radius) {
          found = node;
          break;
        }
      }
      this.hoveredNode = found;
      this.canvas.style.cursor = found ? 'pointer' : 'default';
    });
  }

  updateTelemetry(telemetry) {
    if (!telemetry) return;
    this.state.isOnline = telemetry.online !== false;
    
    if (telemetry.solar_total_w !== undefined) {
      this.state.solarW = telemetry.solar_total_w;
    }
    if (telemetry.pv1_east) {
      this.state.pv1W = telemetry.pv1_east.power_w;
    }
    if (telemetry.pv2_west) {
      this.state.pv2W = telemetry.pv2_west.power_w;
    }
    if (telemetry.grid) {
      this.state.gridExportW = telemetry.grid.grid_export_w || 0;
      this.state.gridImportW = telemetry.grid.grid_import_w || 0;
      this.state.homeLoadW = telemetry.grid.home_load_w || 220;
    }
    if (telemetry.battery) {
      this.state.batSoc = telemetry.battery.soc_percent !== undefined ? telemetry.battery.soc_percent : 42;
      this.state.batVoltage = telemetry.battery.voltage_v || 196.0;
      this.state.batPowerW = telemetry.battery.power_w !== undefined ? telemetry.battery.power_w : 0;
      this.state.batEtaInfo = telemetry.batEtaInfo || null;
    }
    if (telemetry.inverter) {
      this.state.invTemp = telemetry.inverter.temperature_c || 35.0;
    }
    if (telemetry.ev_status) {
      this.state.evChargeW = telemetry.ev_status.is_charging ? (telemetry.ev_status.ev_power_w || 3000) : 0;
      this.state.evSoc = telemetry.ev_status.current_soc_pct;
      this.state.evKm = telemetry.ev_status.ev_range_km;
      this.state.evIsCharging = telemetry.ev_status.is_charging;
    } else if (telemetry.grid && telemetry.grid.home_load_w >= 1800) {
      this.state.evChargeW = Math.max(0, telemetry.grid.home_load_w - 320);
      this.state.evIsCharging = true;
    } else {
      this.state.evChargeW = 0;
      this.state.evIsCharging = false;
    }
  }

  startAnimation() {
    const loop = (now) => {
      const dt = Math.min((now - this.lastTime) / 1000, 0.1);
      this.lastTime = now;

      this.updateParticles(dt);
      this.draw();

      this.animationId = requestAnimationFrame(loop);
    };
    this.animationId = requestAnimationFrame(loop);
  }

  updateParticles(dt) {
    // 1. Flujo Solar -> Inversor
    if (this.state.solarW > 20) {
      const speed = 0.4 + (this.state.solarW / 5000) * 1.5;
      if (Math.random() < Math.min(0.85, (this.state.solarW / 3000))) {
        this.particles.push({
          from: this.nodes.solar,
          to: this.nodes.inverter,
          progress: 0,
          speed,
          color: '#fbbf24',
          size: 3.5
        });
      }
    }

    // 2. Flujo Inversor -> Hogar
    if (this.state.homeLoadW > 20) {
      const speed = 0.4 + (this.state.homeLoadW / 3000) * 1.5;
      if (Math.random() < Math.min(0.8, (this.state.homeLoadW / 2000))) {
        this.particles.push({
          from: this.nodes.inverter,
          to: this.nodes.home,
          progress: 0,
          speed,
          color: '#10b981',
          size: 3.0
        });
      }
    }

    // 3. Flujo Batería <-> Inversor
    if (Math.abs(this.state.batPowerW) > 30 || (this.state.homeLoadW > this.state.solarW && this.state.batSoc > 10)) {
      const isCharging = this.state.solarW > this.state.homeLoadW;
      const flowW = Math.abs(this.state.batPowerW) || Math.abs(this.state.homeLoadW - this.state.solarW);
      const speed = 0.4 + (flowW / 3000) * 1.2;
      if (Math.random() < 0.4) {
        this.particles.push({
          from: isCharging ? this.nodes.inverter : this.nodes.battery,
          to: isCharging ? this.nodes.battery : this.nodes.inverter,
          progress: 0,
          speed,
          color: '#c084fc',
          size: 3.2
        });
      }
    }

    // 4. Flujo Inversor <-> Red / Batería Virtual
    if (this.state.gridExportW > 50) {
      const speed = 0.4 + (this.state.gridExportW / 4000) * 1.4;
      if (Math.random() < Math.min(0.7, this.state.gridExportW / 2500)) {
        this.particles.push({
          from: this.nodes.inverter,
          to: this.nodes.grid,
          progress: 0,
          speed,
          color: '#06b6d4',
          size: 3.2
        });
      }
    } else if (this.state.gridImportW > 50) {
      if (Math.random() < 0.3) {
        this.particles.push({
          from: this.nodes.grid,
          to: this.nodes.inverter,
          progress: 0,
          speed: 0.6,
          color: '#f43f5e',
          size: 3.0
        });
      }
    }

    // 5. Flujo Inversor -> Omoda 7 SHS (si está cargando)
    if (this.state.evChargeW > 100) {
      if (Math.random() < 0.5) {
        this.particles.push({
          from: this.nodes.inverter,
          to: this.nodes.ev,
          progress: 0,
          speed: 0.8,
          color: '#ec4899',
          size: 3.0
        });
      }
    }

    // Actualizar progreso de partículas
    for (let i = this.particles.length - 1; i >= 0; i--) {
      const p = this.particles[i];
      p.progress += p.speed * dt;
      if (p.progress >= 1) {
        this.particles.splice(i, 1);
      }
    }
  }

  draw() {
    const ctx = this.ctx;
    const w = this.width;
    const h = this.height;

    ctx.clearRect(0, 0, w, h);

    // Fondo suave con gradiente sutil
    const bgGrad = ctx.createLinearGradient(0, 0, w, h);
    bgGrad.addColorStop(0, 'rgba(15, 23, 42, 0.7)');
    bgGrad.addColorStop(1, 'rgba(11, 15, 25, 0.9)');
    ctx.fillStyle = bgGrad;
    ctx.fillRect(0, 0, w, h);

    // 1. Dibujar líneas de conexión fijas
    this.drawConnections();

    // 2. Dibujar partículas animadas
    this.drawParticles();

    // 3. Dibujar nodos interactivos
    this.drawNodes();

    // 4. Dibujar tooltip si hay hover
    if (this.hoveredNode) {
      this.drawTooltip(this.hoveredNode);
    }
  }

  drawConnections() {
    const ctx = this.ctx;
    const pairs = [
      [this.nodes.solar, this.nodes.inverter, this.state.solarW > 20],
      [this.nodes.inverter, this.nodes.battery, Math.abs(this.state.batPowerW) > 30],
      [this.nodes.inverter, this.nodes.home, this.state.homeLoadW > 20],
      [this.nodes.inverter, this.nodes.grid, this.state.gridExportW > 50 || this.state.gridImportW > 50],
      [this.nodes.inverter, this.nodes.ev, this.state.evChargeW > 100]
    ];

    pairs.forEach(([from, to, isActive]) => {
      ctx.beginPath();
      if (isActive && to === this.nodes.ev) {
        ctx.lineWidth = 3.5;
        ctx.strokeStyle = 'rgba(236, 72, 153, 0.85)'; // Neón rosa activo para Omoda 7
        ctx.setLineDash([]);
      } else if (isActive) {
        ctx.lineWidth = 2.5;
        ctx.strokeStyle = 'rgba(255, 255, 255, 0.28)';
        ctx.setLineDash([]);
      } else {
        ctx.lineWidth = 2.0;
        ctx.strokeStyle = 'rgba(255, 255, 255, 0.10)';
        ctx.setLineDash([4, 4]);
      }
      ctx.moveTo(from.x, from.y);
      ctx.lineTo(to.x, to.y);
      ctx.stroke();
      ctx.setLineDash([]);
    });
  }

  drawParticles() {
    const ctx = this.ctx;
    this.particles.forEach(p => {
      const x = p.from.x + (p.to.x - p.from.x) * p.progress;
      const y = p.from.y + (p.to.y - p.from.y) * p.progress;

      ctx.save();
      ctx.beginPath();
      ctx.arc(x, y, p.size, 0, Math.PI * 2);
      ctx.fillStyle = p.color;
      ctx.shadowColor = p.color;
      ctx.shadowBlur = 8;
      ctx.fill();
      ctx.restore();
    });
  }

  drawNodes() {
    const ctx = this.ctx;

    for (const key in this.nodes) {
      const node = this.nodes[key];
      const isHovered = this.hoveredNode && this.hoveredNode.id === node.id;

      // Actualizar halo dinámico según estado activo
      if (node.id === 'ev') {
        const isCharging = this.state.evChargeW > 100;
        node.glow = isCharging ? 'rgba(236, 72, 153, 0.85)' : 'rgba(236, 72, 153, 0.25)';
      }

      ctx.save();

      // Halo resplandor
      ctx.beginPath();
      ctx.arc(node.x, node.y, node.radius + (isHovered ? 6 : 2), 0, Math.PI * 2);
      ctx.fillStyle = node.glow;
      ctx.shadowColor = node.color;
      ctx.shadowBlur = isHovered ? 20 : (node.id === 'ev' && this.state.evChargeW > 100 ? 18 : 12);
      ctx.fill();

      // Círculo principal
      ctx.beginPath();
      ctx.arc(node.x, node.y, node.radius, 0, Math.PI * 2);
      ctx.fillStyle = '#1e293b';
      ctx.strokeStyle = node.color;
      ctx.lineWidth = isHovered || (node.id === 'ev' && this.state.evChargeW > 100) ? 3 : 2;
      ctx.fill();
      ctx.stroke();

      // Textos del nodo
      ctx.textAlign = 'center';
      ctx.fillStyle = '#f8fafc';
      ctx.font = 'bold 10.5px system-ui, sans-serif';
      ctx.fillText(node.label, node.x, node.y - 4);

      // Subtexto de valor dinámico
      let dynamicVal = '';
      if (node.id === 'solar') dynamicVal = `${(this.state.solarW / 1000).toFixed(2)} kW`;
      if (node.id === 'inverter') dynamicVal = `${this.state.invTemp}°C • OK`;
      if (node.id === 'battery') {
        if (this.state.batSoc >= 99) {
          dynamicVal = '100% (Llena)';
        } else if (this.state.batEtaInfo && this.state.batEtaInfo.isCharging) {
          dynamicVal = `${this.state.batSoc}% (${this.state.batEtaInfo.etaTimeStr})`;
        } else {
          dynamicVal = `${this.state.batSoc}% SoC`;
        }
      }
      if (node.id === 'home') dynamicVal = `${(this.state.homeLoadW / 1000).toFixed(2)} kW`;
      if (node.id === 'grid') dynamicVal = this.state.gridExportW > 0 ? `+${(this.state.gridExportW / 1000).toFixed(2)} kW` : (this.state.gridImportW > 0 ? `-${(this.state.gridImportW / 1000).toFixed(2)} kW` : '0.0 kW');
      if (node.id === 'ev') dynamicVal = this.state.evChargeW > 100 ? `+${(this.state.evChargeW / 1000).toFixed(2)} kW (${this.state.evSoc || 18}%)` : `${this.state.evSoc || 18}% SoC`;

      ctx.fillStyle = node.color;
      ctx.font = '800 10px monospace';
      ctx.fillText(dynamicVal, node.x, node.y + 12);

      ctx.restore();
    }
  }

  drawTooltip(node) {
    const ctx = this.ctx;
    let title = node.label;
    let lines = [];

    if (node.id === 'solar') {
      lines = [
        `• String 1 Este (85°): ${(this.state.pv1W / 1000).toFixed(2)} kW (6x Jinko 500W)`,
        `• String 2 Oeste (265°): ${(this.state.pv2W / 1000).toFixed(2)} kW (4x Jinko 500W)`,
        `• Potencia Total: ${(this.state.solarW / 1000).toFixed(3)} kW`
      ];
    } else if (node.id === 'inverter') {
      lines = [
        `• Modelo: Sunworks KP10 SW (Fox-ESS 10kW)`,
        `• Conexión: Modbus TCP 192.168.1.66:502 (ID 247)`,
        `• Temperatura: ${this.state.invTemp} °C`
      ];
    } else if (node.id === 'battery') {
      const etaStr = this.state.batEtaInfo ? (this.state.batEtaInfo.isCharging ? `100% a las ${this.state.batEtaInfo.etaTimeStr} (en ${this.state.batEtaInfo.timeRemainingStr})` : (this.state.batEtaInfo.isFull ? '100% Llena (Autonomía >48h)' : `Autonomía ~${this.state.batEtaInfo.autonomyHours}`)) : 'Calculando...';
      const energyNeeded = this.state.batEtaInfo ? `${this.state.batEtaInfo.energyNeededKwh} kWh` : '-- kWh';
      lines = [
        `• Modelo: 2x Fox-ESS EP5 High Voltage (10.36 kWh)`,
        `• Estado de Carga (SoC): ${this.state.batSoc} %`,
        `• Tensión de Pack: ${this.state.batVoltage} V`,
        `• Estimación Carga 100%: ${etaStr}`,
        `• Energía Restante p/ 100%: ${energyNeeded}`
      ];
    } else if (node.id === 'home') {
      lines = [
        `• Consumo Total Hogar: ${this.state.homeLoadW} W`,
        `• Daikin Salón (25°C): ~520 W`,
        `• Daikin Dormitorio (26.5°C): ~210 W`,
        `• Frigorífico Midea 2 Puertas: ~90 W`,
        `• Portátiles teletrabajo + Router: ~145 W`
      ];
    } else if (node.id === 'grid') {
      lines = [
        `• Excedente Neta a Red: ${(this.state.gridExportW / 1000).toFixed(3)} kW`,
        `• Batería Virtual: Acumulando saldo monetario`,
        `• Importación de Red: ${this.state.gridImportW} W (0.00 €)`
      ];
    } else if (node.id === 'ev') {
      lines = [
        `• Vehículo: Omoda 7 SHS (PHEV)`,
        `• Batería: 18.7 kWh (95 km autonomía EV)`,
        `• Carga inteligente: 100% Excedente Solar`
      ];
    }

    const boxW = 260;
    const boxH = 26 + lines.length * 16;
    let boxX = node.x - boxW / 2;
    let boxY = node.y - node.radius - boxH - 10;

    if (boxX < 10) boxX = 10;
    if (boxX + boxW > this.width - 10) boxX = this.width - boxW - 10;
    if (boxY < 10) boxY = node.y + node.radius + 10;

    ctx.save();
    ctx.fillStyle = 'rgba(15, 23, 42, 0.95)';
    ctx.strokeStyle = node.color;
    ctx.lineWidth = 1.5;
    ctx.shadowColor = 'rgba(0,0,0,0.5)';
    ctx.shadowBlur = 12;

    // Caja redondeada
    ctx.beginPath();
    ctx.roundRect(boxX, boxY, boxW, boxH, 8);
    ctx.fill();
    ctx.stroke();

    ctx.fillStyle = '#f8fafc';
    ctx.font = 'bold 11px system-ui, sans-serif';
    ctx.textAlign = 'left';
    ctx.fillText(title, boxX + 12, boxY + 18);

    ctx.fillStyle = '#94a3b8';
    ctx.font = '10px monospace';
    lines.forEach((line, i) => {
      ctx.fillText(line, boxX + 12, boxY + 36 + i * 16);
    });

    ctx.restore();
  }
}
