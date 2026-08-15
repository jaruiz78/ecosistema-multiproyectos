# Kata 03: H3 Surge (Uber H3 & GeoSpatial)

## Objetivo
Indexación y cruce de oferta/demanda en mallas hexagonales H3.

## Reglas
- Operaciones a escala requieren BigQuery GIS u OSRM local.
- Agrupar por `h3_to_parent` para agregación eficiente.
