# Módulo 4 - Lección 3: Indexación Espacial Hexagonal Uber H3 & Algoritmos de Surge Pricing

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Por qué Hexágonos sobre una Esfera R3?
Imagina intentar envolver un balón de fútbol con papel de regalo cuadrado. El papel se arruga en las esquinas porque un plano plano no se pliega perfectamente sobre una esfera.

**Uber H3** resuelve esto proyectando la superficie del planeta Tierra sobre un **Icosaedro** (un poliedro regular de 20 caras triangulares planas en el espacio tridimensional $R^3$). Cada cara del icosaedro se subdivide geométricamente en celdas hexagonales mediante la distancia geodésica del gran círculo.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Proyección Geodésica H3 (Esfera R3 a Icosaedro)
        SPHERE["Esfera Terrestre R3 (Latitud ϕ, Longitud λ)"]
        ICOS["Icosaedro de 20 Caras Triangulares Flat"]
        HEX["Teselación Hexagonal (12 Pentágonos de Cierre)"]
    end

    SPHERE -->|Transformación Geodésica| ICOS
    ICOS -->|Subdivisión de Resolución 0 a 15| HEX
```

---

## 3. 🔬 Fundamentación Matemática y Geometría Icosaédrica

### A. Conversión de Coordenadas Esféricas a Espacio Tridimensional $R^3$
Para una latitud $\phi$ y longitud $\lambda$, la posición sobre la esfera de radio de la Tierra $R_{Earth} \approx 6371 \text{ km}$ es:

$$\mathbf{X} = \begin{pmatrix} x \\ y \\ z \end{pmatrix} = \begin{pmatrix} R \cdot \cos\phi \cdot \cos\lambda \\ R \cdot \cos\phi \cdot \sin\lambda \\ R \cdot \sin\phi \end{pmatrix}$$

### B. Proyección sobre el Icosaedro y Distancia Geodésica (Haversine)
La distancia geodésica de gran círculo $d$ entre dos puntos $\mathbf{X}_1(\phi_1, \lambda_1)$ y $\mathbf{X}_2(\phi_2, \lambda_2)$ se calcula analíticamente mediante la fórmula del **Haversine**:

$$\text{haversin}\left(\frac{d}{R}\right) = \text{haversin}(\Delta \phi) + \cos\phi_1 \cdot \cos\phi_2 \cdot \text{haversin}(\Delta \lambda)$$

donde $\text{haversin}(\theta) = \sin^2\left(\frac{\theta}{2}\right)$.

### C. Por qué se necesitan 12 Pentágonos
Según el **Teorema del Carácter de Euler** para poliedros esféricos ($V - E + F = 2$), es matemáticamente imposible cubrir una esfera cerrada usando *únicamente* hexágonos regulares. H3 introduce exactamente **12 pentágonos** colocados en los 12 vértices del icosaedro para garantizar el cierre geométrico completo del planeta.

---

## 4. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```python
import h3
import math

def calculate_h3_surge_multiplier(
    lat: float,
    lng: float,
    demand_map: dict[str, int],
    supply_map: dict[str, int],
    resolution: int = 8
) -> float:
    # 1. Proyección de lat/lng al índice H3 esférico de resolución especificada
    center_hex = h3.geo_to_h3(lat, lng, resolution)
    
    local_demand = demand_map.get(center_hex, 0)
    local_supply = supply_map.get(center_hex, 0)
    local_surge = (local_demand - local_supply) / max(1, local_supply)

    # 2. Obtener el anillo kRing(1) de 6 celdas vecinas a distancia geodésica constante
    neighbors = h3.k_ring(center_hex, 1) - {center_hex}
    neighbor_surges = [(demand_map.get(n, 0) - supply_map.get(n, 0)) / max(1, supply_map.get(n, 0)) for n in neighbors]
    avg_neighbor_surge = sum(neighbor_surges) / len(neighbor_surges) if neighbor_surges else 0.0

    alpha, beta = 0.5, 0.25
    multiplier = 1.0 + alpha * max(0, local_surge) + beta * max(0, avg_neighbor_surge)
    return min(3.5, round(multiplier, 2))
```

---

## 5. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Resolución H3 y Métricas Geodésicas Físicas

| Resolución H3 | Área Promedio de Celda | Distancia de Centroides | Número Total de Celdas en la Tierra |
| :--- | :--- | :--- | :--- |
| **Res 0** | $4,357,449.42 \text{ km}^2$ | 1,107 km | 122 celdas |
| **Res 7** | $5.16 \text{ km}^2$ | 2.8 km | 98,970,742 celdas |
| **Res 8** | **$0.73 \text{ km}^2$** | **1.0 km** | **692,795,212 celdas (AppViajes)** |
| **Res 15** | $0.0000009 \text{ km}^2$ ($0.9 \text{ m}^2$) | 0.5 m | 569,707,781,918,622 celdas |

---

## 6. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Operar sobre una celda pentagonal intentando obtener 6 vecinos**:
   * *Síntoma*: En las 12 celdas pentagonales del planeta, `k_ring(1)` devuelve solo **5 vecinos** en lugar de 6.
   * *Solución*: Utiliza siempre las funciones nativas de la librería H3 (`h3.k_ring()`) sin asumir de forma hardcodeada un array fijo de 6 elementos.
