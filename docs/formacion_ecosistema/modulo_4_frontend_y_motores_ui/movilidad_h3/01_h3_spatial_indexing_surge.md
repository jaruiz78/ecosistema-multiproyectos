# Módulo 4 - Lección 1: Indexación Espacial Hexagonal Uber H3 & Algoritmos de Surge Pricing

---

## 1. 🐣 Ancla Mental Feynman & Analogía Isomórfica

### El Modelo Intuitivo: Indexación Espacial Hexagonal Uber H3 & Algoritmos de Surge Pricing
Para comprender **Indexación Espacial Hexagonal Uber H3 & Algoritmos de Surge Pricing** sin caer en la trampa de la jerga técnica, debemos anclar el concepto en un problema físico observable:
* Todo sistema en computación resuelve un dilema fundamental: cómo organizar recursos limitados (tiempo de cálculo, espacio de memoria, ancho de banda o energía) para que el trabajo se realice sin bloqueos ni errores.
* En **Indexación Espacial Hexagonal Uber H3 & Algoritmos de Surge Pricing**, la clave reside en eliminar pasos redundantes y asegurar que cada componente conozca únicamente la información mínima indispensable para cumplir su función, tal como una línea de montaje bien coordinada donde nadie tiene que adivinar qué hizo el compañero anterior.

---


## 1. ¿Por qué Indexación Hexagonal H3?

En aplicaciones de movilidad urbana (`AppViajes`), dividir el espacio geográfico en cuadrículas rectangulares tradicionales (latitud/longitud) introduce distorsiones severas: los vecinos diagonales están más distantes que los vecinos ortogonales.

**H3 (Uber Hexagonal Spatial Index)** resuelve esto: todos los hexágonos adyacentes tienen la misma distancia exacta entre sus centroides.

```mermaid
graph TD
    subgraph Malla Hexagonal H3 (Resolución 8)
        H0["Hexágono Central (Celda H3)"]
        H1["Vecino 1 (Distancia D)"]
        H2["Vecino 2 (Distancia D)"]
        H3["Vecino 3 (Distancia D)"]
        H4["Vecino 4 (Distancia D)"]
        H5["Vecino 5 (Distancia D)"]
        H6["Vecino 6 (Distancia D)"]
    end

    H0 --- H1
    H0 --- H2
    H0 --- H3
    H0 --- H4
    H0 --- H5
    H0 --- H6
```

---

## 2. Algoritmo de Tarifa Dinámica (Surge Pricing) por Celda H3

El multiplicador de tarifa dinámica ($S_c$) para una celda H3 $c$ se calcula cruzando la demanda activa ($D_c$) y la oferta de conductores disponibles ($O_c$):

$$S_c = \max\left(1.0, \, 1.0 + \alpha \cdot \frac{D_c - O_c}{O_c + \epsilon} + \beta \sum_{n \in \text{kRing}(c, 1)} \frac{D_n}{O_n + \epsilon}\right)$$

donde $\alpha$ es la sensibilidad local, $\beta$ la influencia del anillo de vecinos ($\text{kRing}$ de radio 1), y $\epsilon = 0.001$ evita divisiones por cero.

---

## 3. Código en Python / Dart para Cálculo de Surge Index

```python
import h3

def calculate_h3_surge_multiplier(
    lat: float,
    lng: float,
    demand_map: dict[str, int],
    supply_map: dict[str, int],
    resolution: int = 8
) -> float:
    """
    Calcula el multiplicador de tarifa dinámica para la ubicación (lat, lng)
    utilizando celdas H3 y suavizado por kRing(1).
    """
    # 1. Obtener la celda H3 correspondiente a las coordenadas
    center_hex = h3.geo_to_h3(lat, lng, resolution)

    # 2. Oferta y Demanda local
    local_demand = demand_map.get(center_hex, 0)
    local_supply = supply_map.get(center_hex, 0)

    # 3. Factor de escasez local
    local_surge = (local_demand - local_supply) / max(1, local_supply)

    # 4. Suavizado con anillo kRing(1) de celdas vecinas
    neighbors = h3.k_ring(center_hex, 1) - {center_hex}
    neighbor_surges = []
    for n_hex in neighbors:
        n_d = demand_map.get(n_hex, 0)
        n_s = supply_map.get(n_hex, 0)
        neighbor_surges.append((n_d - n_s) / max(1, n_s))

    avg_neighbor_surge = sum(neighbor_surges) / len(neighbor_surges) if neighbor_surges else 0.0

    # 5. Multiplicador final acotado
    alpha, beta = 0.5, 0.25
    multiplier = 1.0 + alpha * max(0, local_surge) + beta * max(0, avg_neighbor_surge)
    return min(3.5, round(multiplier, 2))
```


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Indexación Espacial Hexagonal Uber H3 & Algoritmos de Surge Pricing** a un estudiante de secundaria, **sin usar las palabras:** "Indexación", "Espacial", "Hexagonal" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
