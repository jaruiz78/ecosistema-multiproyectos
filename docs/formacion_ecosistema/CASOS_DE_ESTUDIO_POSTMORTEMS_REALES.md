# 💥 Casos de Estudio de Postmortems Reales & Inyección de Fallos
## *Universidad Privada del Ecosistema: Cátedra de Resiliencia SRE & Anti-Fragilidad*

Aprender de las catástrofes de ingeniería de software más costosas de la historia es fundamental para diseñar sistemas inmunes a fallos en cascada. Cada caso analiza: la causa raíz física, el impacto económico, la solución arquitectónica y la regla corporativa aplicable en nuestro ecosistema.

---

### 1. 🌐 El Apagón Global de Cloudflare (2019): Backtracking Catastrófico ReDoS
* **Causa Raíz:** Una regla de firewall (WAF) contenía una expresión regular mal formada `.*(?:.*=.*)` evaluando texto no confiable. El motor de regex NFA (No Determinista) ejecutó una búsqueda con retroceso exponencial \(\mathcal{O}(2^N)\), alcanzando el 100% de uso de CPU en todos los servidores perimetrales a nivel mundial.
* **Impacto:** Caída de más del 12% del tráfico de internet durante 27 minutos.
* **Lección & Solución:** 
  1. Prohibir expresiones regulares no lineales en rutas críticas.
  2. Usar motores basados en Autómatas Finitos Deterministas (DFA) con garantía de tiempo lineal \(\mathcal{O}(N)\) (como Go `regexp` o RE2 de Google).
* **Regla en Ecosistema:** Ningún endpoint o filtro BFF puede ejecutar regex recursivas en hilos de procesamiento de peticiones.

---

### 2. ☕ El Incidente de Carrier Thread Pinning en Java 21 (2023)
* **Causa Raíz:** Una biblioteca de acceso a base de datos utilizaba métodos tradicionales con `synchronized` en lugar de `ReentrantLock`. Cuando miles de Virtual Threads bloquearon en operaciones de I/O de red dentro de esos bloques, anclaron (*pinned*) los hilos portadores del ForkJoinPool del SO, causando agotamiento total de hilos (*Thread Pool Starvation*).
* **Impacto:** Latencias superiores a 30 segundos y congelación de microservicios con 0% de uso de CPU real.
* **Lección & Solución:**
  1. En Java 21-23: Sustitución de `synchronized` por `ReentrantLock`.
  2. En Java 25 (LTS): Integración nativa del **JEP 491** (*Synchronize Virtual Threads without Pinning*), permitiendo el desanclaje de continuaciones sin congelar hilos del SO.
* **Regla en Ecosistema:** Código de dominio 100% puro; adaptadores I/O gobernados por `java.util.concurrent.locks.ReentrantLock` o Java 25 nativo.

---

### 3. 💳 El Error de Redondeo Financiero: Float vs Céntimos Enteros
* **Causa Raíz:** Uso del tipo primitivo IEEE 754 `double` o `float` en punto flotante binario para almacenar saldos monetarios. En binario, \(0.1 + 0.2 = 0.30000000000000004\). Tras millones de micro-transacciones, el error acumulado genera descuadres contables y vulnerabilidades de explotación (*Salami slicing*).
* **Impacto:** Millones de dólares en pérdidas contables y sanciones regulatorias bancarias.
* **Lección & Solución:**
  1. Los saldos y precios deben expresarse **SIEMPRE como enteros de menor denominación** (céntimos: `1050` para `10.50 EUR`) usando `long` en Java/Go.
  2. En cálculos fraccionarios de intereses, usar aritmética decimal exacta (`BigDecimal` con `RoundingMode.HALF_EVEN`).
* **Regla en Ecosistema:** Prohibido el uso de `float`/`double` en cualquier entidad de facturación o integración con Stripe Connect.

---

### 4. 📦 El Ataque a la Cadena de Suministro SolarWinds (2020)
* **Causa Raíz:** Inyección de código malicioso en el servidor de compilación (CI/CD) antes de la generación del artefacto final. El código fuente en Git era correcto, pero el binario compilado estaba comprometido porque no existía verificación de procedencia inmutable.
* **Impacto:** Vulneración de 18,000 organizaciones y agencias gubernamentales.
* **Lección & Solución:**
  1. Implementación obligatoria de **SLSA Nivel 3/4** (*Supply-chain Levels for Software Artifacts*).
  2. Generación automática de SBOM (*Software Bill of Materials* en CycloneDX).
  3. Firma criptográfica de contenedores mediante **Sigstore / Cosign** con certificados efímeros OIDC vinculados al commit inmutable.
* **Regla en Ecosistema:** Ningún contenedor se despliega en Cloud Run sin atestación SLSA L3 y firma verificada pre-merge.

---

### 5. 📉 El Colapso de Knight Capital (2012): Despliegues no Atómicos
* **Causa Raíz:** Actualización manual de software en 8 servidores donde 1 servidor se omitió por error humano. El código antiguo reutilizaba una bandera de configuración (*flag*) con un nuevo significado, disparando millones de órdenes erróneas de compra/venta en milisegundos.
* **Impacto:** Pérdida de 440 millones de dólares en 45 minutos y quiebra de la entidad.
* **Lección & Solución:**
  1. Erradicar despliegues manuales; adopción estricta de **GitOps (ArgoCD)** con reconciliación declarativa.
  2. Despliegues inmutables *Blue/Green* o *Canary* con reversión (*rollback*) automática basada en métricas SLI de error.
* **Regla en Ecosistema:** Cero cambios manuales en entornos de producción; toda configuración debe residir en Git.

---

### 6. 🔄 Fuga de Descriptores de Archivo por Recreación de ReverseProxy en Reintentos (Go BFF)
* **Causa Raíz:** En bucles de reintento con backoff exponencial, instanciar `httputil.NewSingleHostReverseProxy` con un nuevo `&http.Transport{}` por cada intento. Cada transporte abre conexiones TCP/TLS efímeras sin pool compartido, consumiendo rápidamente los límites de *File Descriptors* del sistema operativo (`ulimit -n`).
* **Impacto:** Agotamiento de sockets, latencias elevadas por handshakes TLS repetidos y caída de throughput de 24,000 QPS a < 200 QPS.
* **Lección & Solución:**
  1. Reutilizar un `http.Transport` compartido empaquetado en un singleton `ReverseProxy`.
  2. Proteger la inicialización mediante caché de lectura/escritura (`sync.RWMutex`).
* **Regla en Ecosistema:** Prohibido instanciar `http.Transport` o `ReverseProxy` dentro de handlers de peticiones o closures de reintento.

---

### 7. 🐢 Cuello de Botella \(N+1\) y Saturación de Quota en Bases Documentales (Firestore / Datastore)
* **Causa Raíz:** En operaciones de seguimiento o reconciliación en tiempo real, iterar sobre \(N\) identificadores realizando llamadas secuenciales `findById(id)` contra la base de datos documental.
* **Impacto:** Aumento lineal de la latencia de \(O(1)\) a \(O(N)\), saturación de cuotas de lectura de Firestore y coste de facturación multiplicado por el número de entidades.
* **Lección & Solución:**
  1. Exponer métodos de consulta por lotes (`findAllById(Collection<String> ids)`) en los puertos de salida del dominio.
  2. Realizar búsquedas masivas mediante consultas `whereIn` o mapeo asociativo en memoria.
* **Regla en Ecosistema:** Todo puerto de repositorio que consulte múltiples entidades debe implementar una firma batch \(O(1)\).

---

### 8. 🌊 Efecto Avalancha (*Cache Stampede*) por Purga Destructiva al 100% de la Caché
* **Causa Raíz:** Al alcanzar el umbral de capacidad máxima de una caché concurrente en memoria (`len > 5000`), ejecutar `make(map[...])` vaciando el 100% de las entradas.
* **Impacto:** Pérdida inmediata de toda la telemetría viva, 0% de aciertos en caché (*cache miss* masivo) y sobrecarga repentina de las bases de datos subyacentes.
* **Lección & Solución:**
  1. Implementar poda incremental gobernada por TTL (`now.Sub(timestamp) > TTL`), eliminando únicamente las entradas obsoletas.
  2. Mantener intactas las entidades vivas y activas durante la limpieza.
* **Regla en Ecosistema:** Las cachés en memoria concurrentes deben utilizar políticas de desalojo graduales (TTL / LRU) y nunca borrado total síncrono.

---

### 9. 🎯 Divergencia Estocástica del Filtro de Kalman por Matrices Estáticas (Myers-Tapley, 1976)
* **Causa Raíz:** Asumir que la matriz de ruido de proceso \(Q\) y de medición \(R\) son constantes e invariantes en el tiempo frente a perturbaciones del mundo real (tormentas, accidentes de tráfico o picos de demanda).
* **Impacto:** El Filtro de Kalman sobrepondera predicciones desfasadas, la covarianza estocástica diverge (\(P > 0.50\)) y el sistema rechaza incorrectamente estados válidos del Gemelo Digital.
* **Lección & Solución:**
  1. Aplicar el algoritmo adaptativo de **Myers & Tapley (1976)** estimando en tiempo real las covarianzas de las innovaciones muestrales en una ventana deslizante.
  2. Auto-calibrar la ganancia de Kalman dinámicamente sin intervención humana.
* **Regla en Ecosistema:** Todos los filtros de asimilación estocástica del Gemelo Digital deben incorporar auto-tuning Myers-Tapley.

