# ADR-008: Consenso Asíncrono Tolerante a Fallos Bizantinos (aBFT) sobre Topologías DAG-Tangle

## Estado
Aceptado

## Contexto
Los intercambios transaccionales inter-administrativos (GovTech `ProyectoB2G`, Liquidaciones Fiscales `ProyectoTaxComplianceLedger` y Tokenización RWA) requieren consenso determinista e inmutable en redes abiertas con latencias de red impredecibles y posibles nodos maliciosos sin depender de coordinadores centrales tipo Raft que introducen puntos únicos de fallo.

## Decisión
Implementar el motor de consenso **Asynchronous Byzantine Fault Tolerance (aBFT)** sobre estructuras de grafo acíclico dirigido (DAG-Tangle) en [`core-asynchronous-byzantine-consensus`](file:///home/jaruiz/Desarrollo/core/core-asynchronous-byzantine-consensus):
1. Tolerar hasta \(f < n/3\) nodos bizantinos mediante quórum de supermayoría (\(2f + 1\)).
2. Validar cada nueva transacción mediante la confirmación de dos transacciones precedentes en el DAG.
3. Garantizar finalidad probabilística asintótica en tiempo \(\mathcal{O}(1)\) con cero bloqueo de hilos mediante Java 25 Virtual Threads.

## Consecuencias
- **Positivas**: Resiliencia total ante caídas de enlace y particiones de red; ausencia de líder (Leaderless); consumo de CPU \(< 2\%\) en reposo.
- **Negativas**: Mayor consumo de ancho de banda para la difusión de votos de atestación criptográfica, optimizado mediante micro-batching.
