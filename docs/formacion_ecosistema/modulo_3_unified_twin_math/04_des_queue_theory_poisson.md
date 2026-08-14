# Módulo 3 - Lección 4: Simulación de Eventos Discretos (DES), Procesos de Poisson & Teoría de Colas

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### Procesos de Poisson y la Fila del Supermercado
Imagina la caja de un supermercado. Los clientes no entran a un ritmo fijo perfecto (ej. 1 cliente exactamente cada 60 segundos). A veces llegan 3 juntos y luego no entra nadie en 5 minutos.

Un **Proceso de Poisson** es el modelo matemático que describe este tipo de llegadas aleatorias pero independientes con una tasa media $\lambda$. La **Teoría de Colas** nos permite calcular cuántas cajas (servidores $c$) debemos abrir para evitar que la fila supere un tiempo de espera máximo sin gastar dinero pagando cajeros innecesarios.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph LR
    subgraph Proceso de Llegada (Poisson λ)
        ARR[Llegadas Aleatorias]
    end

    subgraph Cola FIFO de Espera (Buffer N)
        Q1[Solicitud 1] --- Q2[Solicitud 2] --- Q3[Solicitud 3]
    end

    subgraph Servidores Activos (Tasa de Servicio μ)
        S1[Servidor / Vehículo 1]
        S2[Servidor / Vehículo 2]
        S3[Servidor / Vehículo c]
    end

    ARR --> Q1
    Q3 --> S1
    Q3 --> S2
    Q3 --> S3
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```python
import random
import math

class DiscreteEventSimulation:
    def __init__(self, arrival_rate_lambda: float, service_rate_mu: float, num_servers: int):
        self.lam = arrival_rate_lambda
        self.mu = service_rate_mu
        self.c = num_servers

    def sample_interarrival_time(self) -> float:
        return -math.log(1.0 - random.random()) / self.lam

    def sample_service_time(self) -> float:
        return -math.log(1.0 - random.random()) / self.mu

    def run_simulation(self, total_time: float) -> dict:
        current_time = 0.0
        server_free_times = [0.0] * self.c
        total_wait_time = 0.0
        completed = 0

        while current_time < total_time:
            current_time += self.sample_interarrival_time()
            earliest_idx = min(range(self.c), key=lambda i: server_free_times[i])
            start_service = max(current_time, server_free_times[earliest_idx])
            wait_time = start_service - current_time

            server_free_times[earliest_idx] = start_service + self.sample_service_time()
            total_wait_time += wait_time
            completed += 1

        return {
            "completed_requests": completed,
            "average_wait_time": total_wait_time / max(1, completed),
            "system_utilization": self.lam / (self.c * self.mu)
        }
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Fórmulas de Erlang-C para Modelo Colas $M/M/c$

| Métrica | Formulación Matemática | Significado Práctico |
| :--- | :--- | :--- |
| **Factor de Utilización (\(\rho\))** | \(\rho = \frac{\lambda}{c \cdot \mu} < 1\) | % de ocupación de los servidores (Debe ser < 1 para estabilidad) |
| **Probabilidad de Espera (\(P_w\))** | \(P_w = \frac{\frac{(c\rho)^c}{c! (1-\rho)}}{\sum_{k=0}^{c-1} \frac{(c\rho)^k}{k!} + \frac{(c\rho)^c}{c! (1-\rho)}}\) | Probabilidad de que una nueva solicitud tenga que hacer cola |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Configurar el factor de utilización $\rho \ge 1$**:
   * *Síntoma*: La longitud de la cola crece hasta el infinito y la simulación explota en memoria.
   * *Solución*: Asegúrate de que el número de servidores $c$ satisface siempre $c \cdot \mu > \lambda$.
