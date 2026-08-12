# Original User Request

## 2026-08-09T09:25:48Z

Auditoría integral, validación, corrección y recompilación exhaustiva de los 4 proyectos del ecosistema corporativo (AppViajes, pctMultiMicroservices, SaaSRegantes, corp-spring-boot-starter).

Working directory: /home/jaruiz/Desarrollo/
Integrity mode: benchmark

## Requirements

### R1. Auditoría Simultánea de Código y Arquitectura
Analizar de manera concurrente los 4 proyectos corporativos. Validar que el código cumpla con las arquitecturas establecidas (ej. DDD Hexagonal) y que no haya dependencias rotas, linting errors ni fallos de integración.

### R2. Validación de Artefactos (Documentación, Despliegues y Simulaciones)
Verificar la consistencia de los manifiestos de GCP, los Dockerfiles, y la documentación. Validar de forma prioritaria los scripts del Gemelo Digital (Neural ODEs, H3) para garantizar su correcta funcionalidad asintótica.

### R3. Prevención de Costes GCP (Infraestructura Controlada)
No se deben ejecutar pruebas de integración que incurran en facturación real de Google Cloud Platform (APIs costosas o BigQuery). Deben emplearse Testcontainers, dry-runs y mocks o entornos emulados (ej. `bq_dry_run` u orquestación local) para todo el testing.

### R4. Auto-Reparación y Testing Obligatorio
El equipo debe corregir de forma autónoma cualquier error identificado. No se da por válida ninguna corrección a menos que los comandos de compilación y los test suite pasen en verde.

## Acceptance Criteria

### Compilación y Sintaxis
- [ ] `mvn clean compile` / `go build` / `flutter build` se ejecutan exitosamente sin errores de dependencias o sintaxis en los 4 repositorios.

### Tests Unitarios e Integración (Zero Cost)
- [ ] Ejecución de `mvn test` y equivalentes finaliza con el 100% de los tests en verde, garantizando que GCP no haya incurrido en facturación real.

### Verificación de Simulaciones
- [ ] Ejecución de las simulaciones (ej. `python3 master_digital_twin.py`) finalizan limpiamente con un *exit code* de 0 y reportan logs válidos.
