# Módulo 6.9: Ingeniería de Software Empírica y Análisis de Fallos (Nivel BTH / Waterloo)

---

## 1. 🐣 Rincón Junior: "A mí en local me funciona"

Cuando un Junior sube código y el sistema se cae, su primera reacción es: *"A mí en mi ordenador me funciona"*.
El problema es que un servidor de producción que maneja 50.000 usuarios concurrentes en 3 zonas geográficas distintas bajo estrés de red no se parece en nada a "tu ordenador".
Para entender por qué fallan los sistemas grandes, no basta con leer el código. Hay que analizar los datos reales.
La **Ingeniería de Software Empírica** (Empirical Software Engineering) trata el desarrollo de software no como un arte o una creencia, sino como una Ciencia Natural. Estudiamos el código a través de la minería de repositorios (Git), el análisis estadístico de bugs y la sociología del equipo de desarrollo.

---

## 2. 🔬 Fundamentos Teóricos: Empirical Software Engineering (ESE)

Las universidades líderes en estudiar los fallos de software en la vida real son la Universidad de Waterloo (Canadá) y el Blekinge Institute of Technology (BTH, Suecia).

El enfoque Empírico se basa en el **MSR (Mining Software Repositories)**:
En lugar de teorizar por qué el código es malo, extraemos millones de commits de GitHub, incidencias de Jira y logs de producción para encontrar correlaciones matemáticas reales.
*Descubrimientos científicos de ESE aplicados al Consilium Romano*:
1.  **La métrica que más predice bugs no es la Complejidad Ciclomática**. Es la **Rotación Organizacional (Organizational Churn)**. Si un archivo fue editado por 15 desarrolladores distintos que ya no están en la empresa, tiene un 80% más de probabilidad de tirar el sistema de producción.
2.  **La Ley de Conway Inversa**: La arquitectura de tu software forzará a tu empresa a comunicarse de la misma manera que el código se comunica. Si tienes microservicios fuertemente acoplados, tendrás equipos de desarrollo pasándose la culpa en reuniones interminables.

---

## 3. 🚀 Arquitectura Práctica: Análisis Forense de Fallos (Waterloo)

En el Gemelo Digital Corporativo, cuando ocurre un fallo catastrófico (un P0 que paraliza la operativa), no hacemos un simple "Postmortem" preguntando quién tuvo la culpa. Hacemos un **Análisis de Fallos Multinivel**.

La Universidad de Waterloo clasifica los fallos de sistemas distribuidos en tres niveles:
1.  **Fail-Stop Failures (Fallos de Parada)**: Un microservicio se estrella por un OOM (Out Of Memory). Es fácil de detectar y el orquestador (Kubernetes) levanta otro.
2.  **Performance Failures (Fallos de Rendimiento)**: El sistema no se estrella, pero la API que antes tardaba 50ms ahora tarda 4 segundos. Estos son mortales porque los timeouts empiezan a propagarse, saturando la red (Cascading Failures).
3.  **Silent Data Corruption (Corrupción Silenciosa de Datos)**: El peor de todos. La API funciona rápido y no hay errores, pero la base de datos está guardando las tarifas H3 con un 10% de error por un problema de truncamiento de coma flotante. El sistema puede operar semanas hasta que alguien se da cuenta del desastre financiero.

**Arquitectura de Mitigación Empírica**:
Para el nivel 3, el Consilium exige que el flujo de datos tenga **Asserts de Dominio Distribuidos**. Un servicio secundario (Shadow Mode) lee las bases de datos de forma pasiva y usa BigQuery ML (Detección de Anomalías) para detectar desviaciones estadísticas en los datos de negocio en tiempo real.

---

## 4. 🧠 Internals Avanzados: Bug Prediction Models y Análisis de Grafo

En BTH, los ingenieros construyen modelos de Inteligencia Artificial que leen el árbol de Git antes de permitir que un Pull Request (PR) se mezcle.

**Grafo de Dependencias Sensibles**:
Extraemos el grafo de conocimiento del código (usando `codebase-memory-mcp` internamente). Si un Junior hace un PR modificando un archivo A, el modelo sabe que el archivo A y el archivo Z fueron modificados juntos en el 90% de los commits históricos (Coupling lógico, no físico).
Si el PR modifica A pero ignora Z, el pipeline de CI/CD lanza una alerta automática basada en probabilidad de regresión.

**Análisis de Antipatrones en tiempo de ejecución (Logs)**:
No miramos los logs solo para ver errores. Analizamos la **Entropía de los Logs**. Si la varianza de los tiempos entre logs consecutivos cambia drásticamente respecto a la línea base histórica, es el predictor empírico número uno de que un fallo en cascada ocurrirá en los próximos 15 minutos (Teoría de Colas y Saturación).

---

## 5. ⚠️ Runbook SRE Corporativo: El Post-mortem Sin Culpa (Blameless) y la Ecuación del Riesgo

**Regla de Waterloo / BTH para Post-mortems**:
"Tú no rompiste el sistema. El sistema estaba configurado para permitirte romperlo."

**Ecuación Empírica del Riesgo de Despliegue**:
$Riesgo = (Impacto \times Probabilidad) \times \frac{Tiempo\_de\_Desarrollo}{Tamaño\_del\_Diff}$

Si un desarrollador hace un Commit de 5.000 líneas que escribió en un solo fin de semana, la probabilidad de fracaso empírico tiende al 100%. 
Por ello, el SRE bloquea mecánicamente PRs de más de 400 líneas.
Si el sistema cae, el Runbook exige:
1. Apagar (Traffic Split 0%) a la versión anterior en menos de 2 minutos.
2. Exportar la telemetría del incidente (Datadog/OTEL) al Data Lake (BigQuery).
3. Extraer el grafo del fallo usando Python (`pandas`, `networkx`).
4. Añadir un test automatizado (JUnit/Testcontainers) que reproduzca matemáticamente las condiciones exactas del fallo para garantizar estadísticamente que nunca más volverá a ocurrir.
