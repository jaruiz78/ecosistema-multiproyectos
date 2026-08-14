# GUÍA DE LABORATORIOS PRÁCTICOS Y VALIDACIÓN EMPÍRICA (PROVE-IT STANDARD)
**Google Antigravity Sovereign Framework** | **Nivel de Rigor:** CMU / MIT / BTH Empirical Benchmark

Esta guía proporciona las instrucciones operativas, comandos de terminal exactos y criterios de aceptación empírica (*Prove-It Standard*) para verificar cada módulo técnico del ecosistema.

---

## 🧪 Laboratorio 1: Java 25 Virtual Threads & Leyden CDS (`Módulo 1`)

### Objetivo
Verificar la capacidad de concurrencia masiva sin *Carrier Thread Pinning* y el tiempo de arranque en frío (*cold-start*) `<80ms`.

### Comandos de Ejecución
```bash
# 1. Compilación y suite de tests Zero-Mockito
cd /home/jaruiz/Desarrollo/corp-spring-boot-starter
mvn clean test -Dtest=*ConcurrencyTest

# 2. Entrenamiento de Class Data Sharing (CDS)
bash scripts/bin/leyden_cds_trainer.sh

# 3. Medición de arranque en frío
java -XX:SharedArchiveFile=target/classes.jsa -jar target/app.jar --dry-run
```

### Criterio de Aceptación
* **Pase:** 0 fallos en tests de concurrencia con 10.000 tareas paralelas; tiempo de inicio reportado en log `<80ms`.
* **Fallo:** Detección de bloqueos `synchronized` o tiempo de inicio `>150ms`.

---

## 🧪 Laboratorio 2: Concurrencia Pura y Escape Analysis en Go (`Módulo 2`)

### Objetivo
Comprobar el paso de mensajes CSP en canales de alta velocidad, cero fugas de memoria y reutilización de buffers con `sync.Pool`.

### Comandos de Ejecución
```bash
# 1. Ejecución de benchmarks con detección de carreras
cd /home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_2_go_y_concurrencia/laboratorios
go test -v -race -bench=. -benchmem

# 2. Análisis de escape a heap
go build -gcflags="-m -m" main.go 2>&1 | grep "escapes to heap"
```

### Criterio de Aceptación
* **Pase:** 0 carreras de datos detectadas (`-race`); asignaciones de memoria por operación `< 16 B/op`.

---

## 🧪 Laboratorio 3: Gemelo Digital y Convergencia EnKF (`Módulo 3`)

### Objetivo
Validar que la asimilación estocástica de telemetría hídrica y de tráfico estabilice la matriz de covarianza por debajo de 0.5.

### Comandos de Ejecución
```bash
# 1. Ejecución del núcleo tensorial maestro
cd /home/jaruiz/Desarrollo/scripts
python3 -m unittest discover -s simulations -p "*test_enkf*.py"

# 2. Verificación telemétrica en SQLite
sqlite3 /home/jaruiz/Desarrollo/scripts/simulations/simulations_telemetry.db \
  "SELECT tick, trace_covariance FROM telemetry_log ORDER BY tick DESC LIMIT 10;"
```

### Criterio de Aceptación
* **Pase:** Valor de `trace_covariance <= 0.45` en el tick 10 tras introducir una perturbación de choque (+300% demanda).

---

## 🧪 Laboratorio 4: Indexación Espacial Uber H3 y Tarifas Dinámicas (`Módulo 4`)

### Objetivo
Calcular multiplicadores de demanda (*Surge Pricing*) en resolución H3-8 y validar ruteo OSRM con latencia sub-milisegundo.

### Comandos de Ejecución
```bash
# 1. Validación de cálculo espacial H3
python3 -c "
import h3
lat, lng = 37.7749, -122.4194
cell = h3.latlng_to_cell(lat, lng, 8)
print(f'H3-8 Index: {cell} | Vecinos: {len(h3.grid_disk(cell, 1))}')
assert len(h3.grid_disk(cell, 1)) == 7
"
```

### Criterio de Aceptación
* **Pase:** Retorno exacto de 7 celdas (celda central + 6 vecinos) y tiempo de cálculo `<50μs`.

---

## 🧪 Laboratorio 5: Seguridad Multi-Tenant RLS y Costes BigQuery (`Módulo 5 & 7`)

### Objetivo
Verificar que ninguna consulta analítica cruce datos entre `tenant_id` y que toda consulta requiera filtro de partición.

### Comandos de Ejecución
```bash
# 1. Auditoría estática de reglas Firestore
python3 scripts/run_sast_audit.py --rules-check

# 2. Dry-Run de consultas BigQuery para validar particionamiento
python3 scripts/bq_dry_run_validator.py --require-partition
```

### Criterio de Aceptación
* **Pase:** 100% de consultas con `requirePartitionFilter = true` y coste estimado `$0.00 USD` en el plan de ejecución.
