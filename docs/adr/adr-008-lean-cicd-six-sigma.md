# ADR 008: Manufactura de Software (Lean CI/CD) y Six Sigma

## Estado
Propuesto

## Contexto
El ecosistema `pctMultiMicroservices` y `corp-spring-boot-starter` involucra despliegues masivos a Cloud Run y compilaciones nativas de Java 25 (AOT/Leyden). Históricamente, las pipelines se construyen orientadas a lograr un *build* exitoso sin monitorizar los desperdicios o cuellos de botella del ciclo. Se requiere aplicar los principios de **Ingeniería de Manufactura y Gestión de Calidad (Six Sigma)** (Referencia: *Toyota Kata, Purdue, CMU*).

## Decisión
1. **Pull System (Kanban/Lean)**: Adoptaremos un flujo de trabajo reactivo en la CI/CD. Los *builds* no se acumularán ciegamente (Push), sino que se compilarán en pequeños lotes (*Small Batch Sizes*) minimizando el Work In Progress (WIP).
2. **Eliminación de Mudas (Desperdicios)**:
   - *Dependencias Fantasma*: Se integrará análisis constante del `pom.xml` y `go.mod` para podar transitivas no utilizadas (basura).
   - *Tiempos Muertos*: La generación de cachés CDS de Leyden se optimizará matemáticamente para no estrangular a la pipeline.
3. **Six Sigma Quality Gate**:
   - Defectos tolerables en SAST/Testcontainers = 0 (Límite de cero falsos negativos).
   - Todo artefacto que supere este control se atestará obligatoriamente con SLSA L3 mediante firmas `Sigstore/Cosign`, garantizando trazabilidad absoluta de la línea de ensamblaje (Supply Chain).

## Consecuencias
*   **Positivas**: Tiempos de compilación menores. Ciclos de *Lead Time* altamente predecibles. Imposibilidad criptográfica de alterar artefactos en producción.
*   **Negativas**: Las compilaciones rotas por violaciones de SLSA o dependencias superfluas retrasarán las entregas si los desarrolladores no aplican higiene continua.

## Validadores (Prove-It)
1. Métricas DORA (Deployment Frequency, Lead Time for Changes) extraídas automáticamente de los logs de Google Cloud Build.
2. Tasa de Fallos por Compilación (Change Failure Rate) acotada bajo estándares Six Sigma.
