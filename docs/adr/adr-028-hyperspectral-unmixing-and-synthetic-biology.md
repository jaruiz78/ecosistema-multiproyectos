# ADR-009: Desmezclado Espectral Satelital N-FINDR y Biología Sintética con Cinética de Hill

## Estado
Aceptado

## Contexto
El monitoreo ambiental de proyectos de descarbonización (`ProyectoCarbonDirectAirCapture`, `ProyectoStratosphericAerosolGeoengineering`) y la síntesis biotecnológica personalizada (`ProyectoSyntheticEnzymeBioFoundry`, `ProyectoClinicalOmicsMultiTenant`) demandan precisión analítica físico-química acoplada en $O(N)$.

## Decisión
Incorporar dos núcleos matemáticos de modelado determinista:
1. **Teledetección Hiperespectral N-FINDR** ([`core-hyperspectral-remote-sensing`](file:///home/jaruiz/Desarrollo/core/core-hyperspectral-remote-sensing)): Extracción de firmas espectrales puras (endmembers) maximizando el volumen de símplex en espacios de dimensión reducida, acoplado con desmezclado lineal (LSMM) para cuantificar abundancias superficiales.
2. **Cinética de Hill no lineal** ([`core-synthetic-biology-gene-circuit`](file:///home/jaruiz/Desarrollo/core/core-synthetic-biology-gene-circuit)): Resolución de ecuaciones diferenciales de Hill para modelar puertas lógicas genéticas (AND, OR, NOT) con coeficientes de cooperatividad \(n \ge 1\).

## Consecuencias
- **Positivas**: Capacidad de validar la mineralización de basalto y absorción de aerosoles desde imágenes satelitales; diseño determinista de biosensores y enzimas desfluorinasas de PFAS.
- **Negativas**: Mayor intensidad computacional en la búsqueda combinatorial N-FINDR, mitigada mediante reducción previa de dimensionalidad PCA.
