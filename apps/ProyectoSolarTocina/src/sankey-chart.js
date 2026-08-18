/**
 * Diagrama Sankey Interactivo de Flujos de Potencia y Balance de Energía en Tiempo Real
 * Muestra visualmente la distribución de la energía fotovoltaica generada hacia:
 * Autoconsumo del Hogar, Carga de Baterías Fox-ESS, Wallbox Omoda 7 y Excedente a Batería Virtual.
 */

class SankeyFlowChart {
  constructor(canvasId) {
    this.canvas = document.getElementById(canvasId);
    this.ctx = this.canvas ? this.canvas.getContext('2d') : null;
    this.data = null;
    this.animationFrame = null;
    this.particles = [];
    this.init();
  }

  init() {
    if (!this.canvas) return;
    this.resize();
    window.addEventListener('resize', () => this.resize());
    this.initParticles();
    this.startAnimation();
  }

  resize() {
    if (!this.canvas) return;
    const rect = this.canvas.parentElement.getBoundingClientRect();
    this.canvas.width = rect.width * window.devicePixelRatio;
    this.canvas.height = 240 * window.devicePixelRatio;
    this.canvas.style.width = `${rect.width}px`;
    this.canvas.style.height = `240px`;
    if (this.ctx) {
      this.ctx.scale(window.devicePixelRatio, window.devicePixelRatio);
    }
  }

  initParticles() {
    this.particles = [];
    for (let i = 0; i < 30; i++) {
      this.particles.push({
        progress: Math.random(),
        speed: 0.005 + Math.random() * 0.008,
        pathIndex: Math.floor(Math.random() * 3),
        size: 2.5 + Math.random() * 1.5
      });
    }
  }

  updateData(sankeyPayload) {
    this.data = sankeyPayload;
  }

  startAnimation() {
    const loop = () => {
      this.draw();
      this.animationFrame = requestAnimationFrame(loop);
    };
    loop();
  }

  draw() {
    if (!this.ctx || !this.canvas) return;
    const width = this.canvas.width / window.devicePixelRatio;
    const height = 240;

    this.ctx.clearRect(0, 0, width, height);

    // Si no hay datos, dibujar placeholder
    if (!this.data || !this.data.links) {
      this.ctx.fillStyle = '#64748b';
      this.ctx.font = '14px system-ui, sans-serif';
      this.ctx.textAlign = 'center';
      this.ctx.fillText('Cargando diagrama de flujos energéticos...', width / 2, height / 2);
      return;
    }

    // Coordenadas de los Nodos
    const leftX = 50;
    const midX = width * 0.40;
    const rightX = width - 80;

    const nodeWidth = 14;
    const nodeHeight = 50;

    const nodes = {
      solar: { x: leftX, y: height * 0.5 - 25, label: '☀️ Paneles Jinko 5 kWp', color: '#f59e0b', val: this.data.links[0]?.value || 2800 },
      inverter: { x: midX, y: height * 0.5 - 25, label: '⚡ Inversor Sunworks 10 kW', color: '#10b981', val: this.data.links[0]?.value || 2800 },
      home: { x: rightX, y: height * 0.20 - 20, label: '🏠 Hogar Daikin + PCs', color: '#38bdf8', val: this.data.links[1]?.value || 1100 },
      battery: { x: rightX, y: height * 0.50 - 20, label: '🔋 Batería Fox-ESS 10.36 kWh', color: '#c084fc', val: this.data.links[2]?.value || 0 },
      grid: { x: rightX, y: height * 0.80 - 20, label: '🌐 Excedente Batería Virtual', color: '#ec4899', val: this.data.links[3]?.value || 1700 }
    };

    // 1. Dibujar Cintas de Flujo (Bezier Curves con degradados)
    const drawRibbon = (fromNode, toNode, color, thickness) => {
      const startX = fromNode.x + nodeWidth;
      const startY = fromNode.y + nodeHeight / 2;
      const endX = toNode.x;
      const endY = toNode.y + nodeHeight / 2;
      const cpX = (startX + endX) / 2;

      this.ctx.beginPath();
      this.ctx.moveTo(startX, startY);
      this.ctx.bezierCurveTo(cpX, startY, cpX, endY, endX, endY);
      this.ctx.lineWidth = Math.max(3, Math.min(22, thickness));
      this.ctx.strokeStyle = color;
      this.ctx.stroke();
    };

    // Cintas
    const solarVal = nodes.solar.val;
    const homeVal = nodes.home.val;
    const batVal = nodes.battery.val;
    const gridVal = nodes.grid.val;

    drawRibbon(nodes.solar, nodes.inverter, 'rgba(245, 158, 11, 0.35)', 18);
    if (homeVal > 0) drawRibbon(nodes.inverter, nodes.home, 'rgba(56, 189, 248, 0.35)', (homeVal / solarVal) * 16);
    if (batVal > 0) drawRibbon(nodes.inverter, nodes.battery, 'rgba(192, 132, 252, 0.35)', (batVal / solarVal) * 16);
    if (gridVal > 0) drawRibbon(nodes.inverter, nodes.grid, 'rgba(236, 72, 153, 0.35)', (gridVal / solarVal) * 16);

    // 2. Partículas animadas viajando por los flujos
    const paths = [
      { from: nodes.solar, to: nodes.inverter, color: '#fcd34d' },
      { from: nodes.inverter, to: nodes.home, color: '#7dd3fc' },
      { from: nodes.inverter, to: nodes.grid, color: '#f472b6' }
    ];

    this.particles.forEach(p => {
      p.progress += p.speed;
      if (p.progress > 1.0) p.progress = 0.0;

      const path = paths[p.pathIndex % paths.length];
      const startX = path.from.x + nodeWidth;
      const startY = path.from.y + nodeHeight / 2;
      const endX = path.to.x;
      const endY = path.to.y + nodeHeight / 2;
      const cpX = (startX + endX) / 2;

      // Calcular posición en curva Bezier cúbica
      const t = p.progress;
      const cx = 3 * (cpX - startX);
      const bx = 3 * (cpX - cpX) - cx;
      const ax = endX - startX - cx - bx;

      const cy = 3 * (startY - startY);
      const by = 3 * (endY - startY) - cy;
      const ay = endY - startY - cy - by;

      const px = Math.pow(1 - t, 3) * startX + 3 * Math.pow(1 - t, 2) * t * cpX + 3 * (1 - t) * Math.pow(t, 2) * cpX + Math.pow(t, 3) * endX;
      const py = Math.pow(1 - t, 3) * startY + 3 * Math.pow(1 - t, 2) * t * startY + 3 * (1 - t) * Math.pow(t, 2) * endY + Math.pow(t, 3) * endY;

      this.ctx.beginPath();
      this.ctx.arc(px, py, p.size, 0, Math.PI * 2);
      this.ctx.fillStyle = path.color;
      this.ctx.shadowColor = path.color;
      this.ctx.shadowBlur = 8;
      this.ctx.fill();
      this.ctx.shadowBlur = 0;
    });

    // 3. Dibujar Nodos Rectangulares y Etiquetas
    Object.values(nodes).forEach(n => {
      // Caja del Nodo
      this.ctx.fillStyle = n.color;
      this.ctx.beginPath();
      this.ctx.roundRect(n.x, n.y, nodeWidth, nodeHeight, 4);
      this.ctx.fill();

      // Etiqueta
      this.ctx.fillStyle = '#f8fafc';
      this.ctx.font = 'bold 11px system-ui, sans-serif';
      this.ctx.textAlign = n.x < width * 0.5 ? 'left' : 'right';
      const labelX = n.x < width * 0.5 ? n.x + nodeWidth + 8 : n.x - 8;
      this.ctx.fillText(n.label, labelX, n.y + 20);

      this.ctx.fillStyle = '#94a3b8';
      this.ctx.font = '11px monospace';
      this.ctx.fillText(`${n.val.toLocaleString('es-ES')} W`, labelX, n.y + 36);
    });
  }
}

window.SankeyFlowChart = SankeyFlowChart;
