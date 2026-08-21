import re

with open("src/power-flow-canvas.js", "r") as f:
    code = f.read()

# 1. Add isPaused state in constructor
code = code.replace(
    "this.hoveredNode = null;",
    "this.hoveredNode = null;\n    this.isPaused = false;\n    this.isMobile = window.innerWidth <= 768;"
)

# 2. Add visibility change listener in initCanvas
code = code.replace(
    "window.addEventListener('resize', () => this.resize());",
    """window.addEventListener('resize', () => {
      this.isMobile = window.innerWidth <= 768;
      this.resize();
    });
    document.addEventListener('visibilitychange', () => {
      if (document.hidden) {
        this.pauseAnimation();
      } else {
        this.resumeAnimation();
      }
    });"""
)

# 3. Update resize() method for responsive height
code = re.sub(
    r'this\.height = 320;',
    'this.height = this.isMobile ? 220 : 320;',
    code
)

# 4. Update computeNodePositions to scale node radius on mobile
code = code.replace(
    "radius: 38,",
    "radius: this.isMobile ? 26 : 38,"
)
code = code.replace(
    "radius: 44,",
    "radius: this.isMobile ? 30 : 44,"
)

# 5. Add pauseAnimation and resumeAnimation methods
new_methods = """  pauseAnimation() {
    this.isPaused = true;
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
  }

  resumeAnimation() {
    if (!this.isPaused) return;
    this.isPaused = false;
    this.lastTime = performance.now();
    this.startAnimation();
  }

  startAnimation() {"""

code = code.replace("  startAnimation() {", new_methods)

# 6. Throttle loop if paused
code = code.replace(
    "const loop = (now) => {",
    """const loop = (now) => {
      if (this.isPaused) return;"""
)

# 7. Throttle max particles on mobile
code = code.replace(
    "this.particles.push({",
    """const maxParticles = this.isMobile ? 16 : 45;
      if (this.particles.length >= maxParticles) return;
      this.particles.push({"""
)

with open("src/power-flow-canvas.js", "w") as f:
    f.write(code)

print("PowerFlowCanvas updated successfully.")
