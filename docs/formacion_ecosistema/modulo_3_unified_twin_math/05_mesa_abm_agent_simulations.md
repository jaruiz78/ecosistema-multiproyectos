# Módulo 3 - Lección 5: Simulación Basada en Agentes (ABM) con Mesa & Perfiles Sintéticos

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es la Simulación Basada en Agentes (ABM)?
Imagina simular un hormiguero. En lugar de intentar escribir una sola ecuación gigante para predecir el movimiento de todo el hormiguero, le das a cada hormiga individual (**Agente**) unas reglas simples (buscar comida, dejar rastro de feromonas, regresar al nido) y observas la estructura que emerge de forma natural.

En **Mesa ABM (Python)**, definimos agentes sintéticos con estados de ánimo, batería y objetivos de ganancias para ver cómo reaccionan ante cambios de tarifas o clima.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Marco de Simulación Mesa
        SCHED[Schedule / Planificador de Ticks]
        GRID[Grid Espacial 2D]
        COLLECTOR[DataCollector / Métricas Macro]
    end

    subgraph Agentes Autónomos Sintéticos (DriverAgent)
        A1[Agente 1: Batería, Humor, Fatiga, Ganancias]
        A2[Agente 2: Batería, Humor, Fatiga, Ganancias]
    end

    SCHED -->|Step Activation| A1
    SCHED -->|Step Activation| A2
    A1 <-->|Posición Espacial| GRID
    A1 -->|Exporta Estado| COLLECTOR
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```python
import mesa
import random

class DriverAgent(mesa.Agent):
    def __init__(self, unique_id, model):
        super().__init__(unique_id, model)
        self.battery = random.uniform(0.3, 1.0)
        self.fatigue = random.uniform(0.0, 0.2)
        self.is_active = True

    def step(self):
        if not self.is_active:
            return
        self.battery -= 0.01
        self.fatigue += 0.02
        if self.battery < 0.05 or self.fatigue > 0.9:
            self.is_active = False

class CityMobilityModel(mesa.Model):
    def __init__(self, num_drivers: int):
        super().__init__()
        self.schedule = mesa.time.RandomActivation(self)
        self.datacollector = mesa.DataCollector(
            model_reporters={"ActiveDrivers": lambda m: sum(1 for a in m.schedule.agents if a.is_active)}
        )

        for i in range(num_drivers):
            agent = DriverAgent(i, self)
            self.schedule.add(agent)

    def step(self):
        self.datacollector.collect(self)
        self.schedule.step()
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Optimización de Planificadores Mesa

| Tipo de Scheduler Mesa | Complejidad Step | Caso de Uso Recomendado |
| :--- | :--- | :--- |
| `BaseScheduler` | \(O(N)\) secuencial fijo | Agentes deterministas ordenados |
| `RandomActivation` | \(O(N)\) orden estocástico aleatorio | Simulaciones humanas / conductoras (Evita sesgo de orden) |
| `SimultaneousActivation` | \(O(N)\) evaluación en 2 fases | Redes donde los agentes reaccionan al estado anterior previo |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Modificar el estado global de la cuadrícula durante la fase de evaluación de `step()`**:
   * *Síntoma*: Sesgos de ejecución donde los primeros agentes procesados tienen ventaja injusta sobre los últimos en el mismo tick.
   * *Solución*: Utiliza `SimultaneousActivation` o separa la fase de cálculo de la de movimiento.
