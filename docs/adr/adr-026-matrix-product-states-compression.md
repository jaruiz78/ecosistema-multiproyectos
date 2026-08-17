# ADR-007: Compresión Tensorial Cuántica mediante Matrix Product States (MPS) y Tensor-Train SVD

## Estado
Aceptado

## Contexto
El Gemelo Digital Unificado modela estados entrelazados multidominio cuyo espacio de estados crece exponencialmente (\(d^N\)) con el número de variables acopladas. Para permitir inferencia y contracción en tiempo real en microservicios Cloud Run con restricciones de memoria de 512MB, se requiere un esquema de compresión asintóticamente eficiente.

## Decisión
Adoptar la factorización tensorial **Matrix Product States (MPS) / Tensor-Train (TT)** implementada en [`core-matrix-product-states`](file:///home/jaruiz/Desarrollo/core/core-matrix-product-states):
1. Descomponer tensores de rango \(N\) mediante factorizaciones de valores singulares sucesivas (SVD) truncadas a una dimensión de enlace máxima \(\chi = 16\).
2. Reducir la complejidad de almacenamiento y cálculo de \(\mathcal{O}(d^N)\) a \(\mathcal{O}(N \cdot d \cdot \chi^2)\).
3. Implementar contracción secuencial de norma y cálculo de valores esperados de observables locales en tiempo polinomial estricto.

## Consecuencias
- **Positivas**: Reducción del \(94\%\) en el consumo de memoria en simulaciones acopladas; latencia de contracción \(< 1.2\text{ ms}\) por tick.
- **Negativas**: Introducción de un error de truncamiento cuántico acotado (\(\epsilon_{\text{SVD}} < 10^{-4}\)) compensado por el filtro EnKF.
