# ADR 002: Estandarización Obligatoria de Indexación Espacial Hexagonal Uber H3

## Estado
Aprobado (Consilium Romano)

## Contexto
El cálculo de densidades de oferta/demanda, ruteo de última milla y parcelas agrícolas requería una partición espacial global sin distorsiones geométricas en diferentes latitudes ni ambigüedad de vecindad presente en mallas cuadrangulares.

## Decisión
Estandarizar el sistema **Uber H3 (Discrete Global Grid System)** como la única representación espacial en todo el ecosistema:
* **H3-7 (~5.16 km²):** Agrupación macroeconómica y balance de comunidades energéticas.
* **H3-8 (~0.73 km²):** Tarifas dinámicas (*Surge Pricing*) y clusters de tráfico en [`AppViajes`](file:///home/jaruiz/Desarrollo/AppViajes).
* **H3-9 (~0.10 km²):** Gestión de parcelas agrícolas en [`SaaSRegantes`](file:///home/jaruiz/Desarrollo/SaaSRegantes) y micro-paradas logísticas.

## Consecuencias
* **Positivas:** Vecindad simétrica uniforme (6 vecinos equidistantes), indexación en enteros `uint64` (búsquedas en $O(1)$) y escalabilidad masiva.
* **Negativas:** Requiere transformaciones proyectivas GeoJSON -> H3 en el punto de ingesta.
