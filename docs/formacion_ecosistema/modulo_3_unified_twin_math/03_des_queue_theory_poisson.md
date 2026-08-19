# Módulo 3 - Lección 3: Simulación de Eventos Discretos (DES), Procesos de Poisson & Teoría de Colas

---

## 1. 🐣 Ancla Mental Feynman & Analogía Isomórfica

### El Modelo Intuitivo: Simulación de Eventos Discretos (DES), Procesos de Poisson & Teoría de Colas
Para comprender **Simulación de Eventos Discretos (DES), Procesos de Poisson & Teoría de Colas** sin caer en la trampa de la jerga técnica, debemos anclar el concepto en un problema físico observable:
* Todo sistema en computación resuelve un dilema fundamental: cómo organizar recursos limitados (tiempo de cálculo, espacio de memoria, ancho de banda o energía) para que el trabajo se realice sin bloqueos ni errores.
* En **Simulación de Eventos Discretos (DES), Procesos de Poisson & Teoría de Colas**, la clave reside en eliminar pasos redundantes y asegurar que cada componente conozca únicamente la información mínima indispensable para cumplir su función, tal como una línea de montaje bien coordinada donde nadie tiene que adivinar qué hizo el compañero anterior.

---


## 1. Modelado Estocástico de Llegadas: Procesos de Poisson

En hubs turísticos (aeropuertos, estaciones de tren en `AppViajes`) y en la distribución de turnos de riego (`SaaSRegantes`), las peticiones o eventos de llegada siguen una distribución de **Proceso de Poisson**.

### Función de Probabilidad de Poisson
La probabilidad de que ocurran $k$ eventos en un intervalo de tiempo $t$ con tasa media de llegada $\lambda$ es:

$$P(N(t) = k) = \frac{(\lambda t)^k e^{-\lambda t}}{k!}$$

El tiempo entre llegadas consecutivas $T$ se distribuye de forma **Exponencial**:
$$f_T(t) = \lambda e^{-\lambda t}, \quad t \ge 0$$

---

## 2. Modelo de Colas $M/M/c$ y Tamaño Óptimo de Buffer

Para calcular el buffer de tolerancia a retrasos sin saturar la flota de vehículos o los canales de irrigación, utilizamos el modelo de colas multi-servidor $M/M/c$.

```mermaid
graph LR
    subgraph Proceso de Llegada (Poisson λ)
        ARR["Llegada de Pasajeros / Peticiones"]
    end

    subgraph Cola FIFO de Espera (Buffer N)
        Q1["Solicitud 1] --- Q2[Solicitud 2"] --- Q3[Solicitud 3]
    end

    subgraph Servidores / Flota (Tasa de Servicio μ)
        S1["Vehículo / Canal 1"]
        S2["Vehículo / Canal 2"]
        S3["Vehículo / Canal c"]
    end

    ARR --> Q1
    Q3 --> S1
    Q3 --> S2
    Q3 --> S3
```

### Fórmulas Clave de Erlang-C:

* **Factor de Utilización del Sistema ($\rho$)**:
  $$\rho = \frac{\lambda}{c \cdot \mu} < 1$$

* **Probabilidad de Espera en Cola (Fórmula Erlang-C)**:
  $$P_w = \frac{\frac{(c\rho)^c}{c! (1-\rho)}}{\sum_{k=0}^{c-1} \frac{(c\rho)^k}{k!} + \frac{(c\rho)^c}{c! (1-\rho)}}$$

---

## 3. Implementación DES con Python (`SimPy` style)

```python
import random
import math

class DiscreteEventSimulation:
    def __init__(self, arrival_rate_lambda: float, service_rate_mu: float, num_servers: int):
        self.lam = arrival_rate_lambda
        self.mu = service_rate_mu
        self.c = num_servers

    def sample_interarrival_time(self) -> float:
        """Muestreo de tiempo entre llegadas exponenciales"""
        return -math.log(1.0 - random.random()) / self.lam

    def sample_service_time(self) -> float:
        """Muestreo de tiempo de servicio exponencial"""
        return -math.log(1.0 - random.random()) / self.mu

    def run_simulation(self, total_time: float) -> dict:
        current_time = 0.0
        queue = []
        server_free_times = [0.0] * self.c
        total_wait_time = 0.0
        completed_requests = 0

        while current_time < total_time:
            interarrival = self.sample_interarrival_time()
            current_time += interarrival

            # Asignar al primer servidor disponible
            earliest_server_idx = min(range(self.c), key=lambda i: server_free_times[i])
            start_service = max(current_time, server_free_times[earliest_server_idx])
            wait_time = start_service - current_time

            service_duration = self.sample_service_time()
            server_free_times[earliest_server_idx] = start_service + service_duration

            total_wait_time += wait_time
            completed_requests += 1

        avg_wait = total_wait_time / max(1, completed_requests)
        return {
            "completed_requests": completed_requests,
            "average_wait_time": avg_wait,
            "system_utilization": (self.lam) / (self.c * self.mu)
        }
```


---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.

---

## ⚙️ Primeros Principios & Fundamentos Conceptuales
1. **Descomposición Atómica:** Cada componente en Módulo 3 - Lección 3: Simulación de Eventos Discretos (DES), Procesos de Poisson & Teoría de Colas se modela de forma determinista y sin estado mutable compartido.
2. **Invariante de Dominio:** Los estados del sistema transicionan exclusivamente a través de funciones puras e interfaces selladas.
3. **Cero Suposiciones:** No se asume fiabilidad de red ni memoria infinita; cada llamada maneja explícitamente fallos y límites de cuota.

