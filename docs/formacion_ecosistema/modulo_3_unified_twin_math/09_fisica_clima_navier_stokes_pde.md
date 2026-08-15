# Módulo 3 - Lección 9: Simulación Física del Clima: Ecuaciones de Navier-Stokes & PDEs

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Cómo funciona el software de simulación del clima?
Imagina que la atmósfera de la Tierra es un recipiente gigante lleno de un fluido (aire y vapor de agua) expuesto al calor del Sol. Para predecir el clima (temperatura, presión, viento, humedad), la física divide la atmósfera en una rejilla de pequeños cubos de aire.

Para cada cubo, el software calcula 3 leyes universales fundamentales:
1. **Conservación de la Masa**: El aire que entra en un cubo debe ser igual al aire que sale.
2. **Conservación del Momento (Fuerza = Masa x Aceleración)**: El viento se mueve debido a diferencias de presión, gravedad y la rotación de la Tierra.
3. **Conservación de la Energía**: El calor del sol calienta el aire, alterando su densidad (el aire caliente sube, el aire frío baja).

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Dominio Físico Atmosférico (Malla 2D/3D)
        GRID[Malla de Diferencias Finitas (nx, ny)]
    end

    subgraph Ecuaciones Diferenciales en Derivadas Parciales (PDEs)
        NS[Ecuación de Navier-Stokes: Campos de Velocidad u, v]
        ADV[Ecuación de Advección-Difusión: Temperatura T y Humedad q]
        POISS[Ecuación de Poisson: Corrección de Presión p]
    end

    subgraph Solver Numérico de Software (Python / NumPy)
        STEP1[1. Advección de Velocidad] --> STEP2[2. Difusión Térmica & Viscosidad]
        STEP2 --> STEP3[3. Corrección de Presión en Poisson]
        STEP3 --> STEP4[4. Actualización del Campo de Viento & Humedad]
    end

    GRID --> NS
    GRID --> ADV
    NS --> STEP1
    POISS --> STEP3
```

---

## 3. 🔬 Fundamentación Física y Derivación Matemática Rigurosa

### A. Ecuaciones de Navier-Stokes para Fluidos Incompresibles

La dinámica del viento en la atmósfera viene gobernada por la ecuación diferencial en derivadas parciales (PDE) de **Navier-Stokes**:

$$\frac{\partial \mathbf{u}}{\partial t} + (\mathbf{u} \cdot \nabla) \mathbf{u} = -\frac{1}{\rho} \nabla p + \nu \nabla^2 \mathbf{u} + \mathbf{g}$$

donde:
* $\mathbf{u} = (u, v)$ es el campo vectorial de velocidad del viento en m/s.
* $p$ es el campo escalar de presión atmosférica.
* $\rho$ es la densidad del aire.
* $\nu$ es la viscosidad cinemática del fluido.
* $\mathbf{g}$ es el vector de aceleración gravitatoria y fuerzas externas (Coriolis).

### B. Ecuación de Advección-Difusión para la Temperatura y Humedad

La evolución de la temperatura $T(x,y,t)$ o humedad $q(x,y,t)$ en la atmósfera se rige por:

$$\frac{\partial T}{\partial t} + \mathbf{u} \cdot \nabla T = \alpha \nabla^2 T + Q_{sol}$$

donde $\mathbf{u} \cdot \nabla T = u \frac{\partial T}{\partial x} + v \frac{\partial T}{\partial y}$ representa la **advección** (transporte de calor por el viento), $\alpha \nabla^2 T$ es la **difusión térmica**, y $Q_{sol}$ es la radiación solar incidente.

### C. Discretización Numérica por Diferencias Finitas (FDM)

Para resolver estas PDEs en código de software, convertimos los operadores continuos en diferencias finitas discretas en una malla espacial de paso $\Delta x, \Delta y$ y paso temporal $\Delta t$:

$$\frac{\partial T}{\partial x} \approx \frac{T_{i+1, j}^n - T_{i-1, j}^n}{2 \Delta x} \quad \text{(Diferencia Central)}$$

$$\nabla^2 T = \frac{\partial^2 T}{\partial x^2} + \frac{\partial^2 T}{\partial y^2} \approx \frac{T_{i+1,j}^n - 2T_{i,j}^n + T_{i-1,j}^n}{\Delta x^2} + \frac{T_{i,j+1}^n - 2T_{i,j}^n + T_{i,j-1}^n}{\Delta y^2}$$

---

## 4. 🚀 Guía Paso a Paso e Implementación Numérica en Software

### Simulator Físico Climático en Python (NumPy Vectorizado)

```python
import numpy as np

class ClimatePhysicsSimulator:
    def __init__(self, nx: int = 100, ny: int = 100, dx: float = 1.0, dy: float = 1.0, dt: float = 0.01, nu: float = 0.1, alpha: float = 0.05):
        self.nx = nx
        self.ny = ny
        self.dx = dx
        self.dy = dy
        self.dt = dt
        self.nu = nu # Viscosidad cinemática
        self.alpha = alpha # Difusividad térmica

        # Campos físicos bidimensionales
        self.u = np.zeros((nx, ny)) # Velocidad viento X
        self.v = np.zeros((nx, ny)) # Velocidad viento Y
        self.p = np.zeros((nx, ny)) # Presión atmosférica
        self.T = np.ones((nx, ny)) * 20.0 # Campo de Temperatura (º C)

        # Inyección de fuente de calor solar en el centro
        self.T[nx//4:nx//2, ny//4:ny//2] += 15.0

    def step_simulation(self):
        """Aplica la discretización de Navier-Stokes y Advección-Difusión en 1 paso de tiempo (dt)"""
        un = self.u.copy()
        vn = self.v.copy()
        Tn = self.T.copy()

        dx, dy, dt, nu, alpha = self.dx, self.dy, self.dt, self.nu, self.alpha

        # 1. Advección y Difusión de Temperatura: ∂T/∂t = - u(∂T/∂x) - v(∂T/∂y) + α ∇²T
        dT_dx = (Tn[2:, 1:-1] - Tn[:-2, 1:-1]) / (2 * dx)
        dT_dy = (Tn[1:-1, 2:] - Tn[1:-1, :-2]) / (2 * dy)
        laplacian_T = (Tn[2:, 1:-1] - 2*Tn[1:-1, 1:-1] + Tn[:-2, 1:-1]) / dx**2 + \
                      (Tn[1:-1, 2:] - 2*Tn[1:-1, 1:-1] + Tn[1:-1, :-2]) / dy**2

        self.T[1:-1, 1:-1] = Tn[1:-1, 1:-1] - dt * (un[1:-1, 1:-1] * dT_dx + vn[1:-1, 1:-1] * dT_dy) + dt * alpha * laplacian_T

        # 2. Paso de Navier-Stokes para Viento u y v
        laplacian_u = (un[2:, 1:-1] - 2*un[1:-1, 1:-1] + un[:-2, 1:-1]) / dx**2 + \
                      (un[1:-1, 2:] - 2*un[1:-1, 1:-1] + un[1:-1, :-2]) / dy**2
        
        self.u[1:-1, 1:-1] = un[1:-1, 1:-1] + dt * nu * laplacian_u

        # Condiciones de contorno periódico (Tierra esférica/cerrada)
        self.T[0, :] = self.T[-1, :] = 20.0
        self.T[:, 0] = self.T[:, -1] = 20.0

if __name__ == "__main__":
    sim = ClimatePhysicsSimulator(nx=50, ny=50)
    for t in range(100):
        sim.step_simulation()
    print(f"Simulación física completada. Temp Máxima tras 100 pasos: {np.max(sim.T):.2f} ºC")
```

---

## 5. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Condición de Estabilidad Courant-Friedrichs-Lewy (CFL Condition)
En solvers numéricos de simulación física, el paso de tiempo $\Delta t$ no puede ser arbitrariamente grande; debe cumplir la condición CFL para garantizar que la información física no viaje más rápido que la malla espacial:

$$C = \frac{u_{\max} \cdot \Delta t}{\Delta x} \le C_{\max} \approx 1.0$$

Si $C > 1.0$, la simulación sufrirá **instabilidad numérica** y los valores de temperatura/viento divergirán a `NaN` o infinito (`inf`).

---

## 6. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Elegir un $\Delta t$ demasiado grande que viole la condición CFL**:
   * *Síntoma*: En pocos pasos de simulación, la matriz `T` o `u` pasa a tener valores `nan` o `inf`.
   * *Solución*: Calcula dinámicamente \(\Delta t \le \frac{0.5 \cdot \Delta x}{\max(|u|, |v|)}\).


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Simulación Física del Clima: Ecuaciones de Navier-Stokes & PDEs** a un estudiante de secundaria, **sin usar las palabras:** "Simulación", "Física", "del" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
