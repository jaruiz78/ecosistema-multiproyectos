# Módulo 4 - Lección 1: Indexación Espacial Hexagonal Uber H3 & Algoritmos de Surge Pricing

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
