# Módulo 6.1: Ingeniería de Confiabilidad del Sitio (SRE) y Matemáticas del Riesgo

---

## 1. 🐣 Rincón Junior: La Perfección es el Enemigo

Si eres un jefe, probablemente le dirás a tu equipo: *"Quiero que el servidor nunca se caiga. 100% de disponibilidad"*.
Google SRE (Site Reliability Engineering) demostró que **apuntar al 100% de disponibilidad es matemática y financieramente estúpido**.
¿Por qué? Porque para pasar del 99.9% al 99.99% (un 9 extra) el coste de infraestructura se multiplica por 10x. Para llegar al 100%, el coste tiende a infinito. Además, los usuarios no notan la diferencia: su WiFi casero o su red 4G fallará mucho antes que tu servidor.
Por lo tanto, el objetivo de un ingeniero no es "cero caídas", sino gestionar el riesgo matemáticamente usando el **Presupuesto de Errores (Error Budget)**.

---

## 2. 🔬 Fundamentos Cuantitativos: SLI, SLO y SLA

SRE es "lo que ocurre cuando le pides a un ingeniero de software que diseñe un equipo de operaciones". Se basa en tres métricas cuantitativas que conforman la base del control de sistemas distribuidos:

1.  **SLI (Service Level Indicator)**: La métrica cruda y matemática extraída directamente de la telemetría (ej. Prometheus). Es una proporción: $\text{SLI} = \frac{\text{Eventos Buenos}}{\text{Eventos Totales}} \times 100$.
    *   *Ejemplo*: "El porcentaje de peticiones HTTP `/reservas` que devolvieron un código `200 OK` en menos de 200 ms durante los últimos 30 días".
2.  **SLO (Service Level Objective)**: Tu objetivo interno, el umbral de dolor del usuario.
    *   *Ejemplo*: "El $\text{SLI}$ debe ser $\ge 99.9\%$ en una ventana móvil de 28 días".
3.  **SLA (Service Level Agreement)**: El contrato legal con el cliente que incluye penalizaciones económicas.
    *   *Regla de Oro*: $\text{SLA} < \text{SLO}$. (Ej. Prometes `$99`.5\%$ legalmente, pero tu SLO de ingeniería es `$99`.9\%$ para tener un margen de seguridad interno antes de perder dinero).

---

## 3. 🚀 Arquitectura Práctica: El Presupuesto de Errores (Error Budget)

Las matemáticas de los "Nueves" (Nines) en una ventana de 30 días:
*   **2 Nueves (99%)**: **7.2 horas** de inactividad permitida. (Batch jobs).
*   **3 Nueves (99.9%)**: **43 minutos**. (E-commerce).
*   **4 Nueves (99.99%)**: **4.3 minutos**. (Pagos, Cloud).
*   **5 Nueves (99.999%)**: **26 segundos**. (Aviación, Telecomunicaciones).

### La Función de Coste del Presupuesto
Si tu SLO es 99.9%, tienes un **Presupuesto de Errores del 0.1%** (43 minutos de fallos totales permitidos al mes). Este presupuesto es una moneda.
*   **El Pacto SRE**: Si el equipo de desarrollo agota su Presupuesto de Errores, la tubería de CI/CD **se congela (Code Freeze) matemáticamente**. Se suspenden todas las subidas de nuevas features a producción. Todo el ancho de banda del equipo debe invertirse en fiabilidad (Testcontainers, Chaos Engineering, fixes de memoria) hasta que la ventana móvil devuelva el presupuesto a valores positivos. Nadie puede forzar un despliegue si las matemáticas dicen "No".

---

## 4. 🧠 Internals SRE Avanzados: Burn Rate, MWW y Estadística Bayesiana

Las alertas clásicas booleanas ("¡CPU al 90%!") son inútiles y causan *Alert Fatigue*. Que la CPU esté alta no significa que el usuario sufra. Las alertas SRE se basan en el **Burn Rate (Tasa de Quemado)** del presupuesto.

$$ \text{Burn Rate} = \frac{\text{Consumo Actual de Errores}}{\text{Consumo Permitido Uniforme}} $$
*   **Burn Rate = 1**: Estás consumiendo tus 43 minutos de forma exacta en 30 días.
*   **Burn Rate = 144**: Estás quemando el 2% de tu presupuesto de 30 días en solo 10 minutos. (Suena el buscapersonas/PagerDuty inmediatamente).

### La Paradoja Bayesiana de las Alertas (Multi-Window Multi-Burn-Rate - MWW)
Alertar sobre una ventana corta de 10 minutos tiene mucho ruido (falsos positivos). Alertar sobre una ventana larga de 1 hora retrasa la detección.
SRE aplica un **Filtro Bayesiano MWW**:
Se evalúa la probabilidad condicional de que el sistema esté realmente roto usando dos ventanas superpuestas (Corto Plazo y Largo Plazo).
La alarma solo suena si:
$$ ( \text{Burn Rate}_\text{5m} > 14.4 ) \land ( \text{Burn Rate}_\text{60m} > 14.4 ) $$
El uso simultáneo de la ventana de 5m anula el rezago (lag) del cálculo de 60m si el fallo recién comienza, mientras que la de 60m actúa como un *Prior Bayesiano* para filtrar picos (spikes) espurios transitorios de 1 minuto que se arreglan solos. Esto garantiza un **Recall del 100%** de fallos críticos con una **Precisión > 99%** para el ingeniero que está durmiendo.

---

## 5. ⚠️ Runbook SRE: MTTD, MTTR y el Teorema del Rollback

Cuando un incidente impacta los Service Level Indicators, medimos la derivada del tiempo:
1.  **MTTD (Mean Time To Detect)**: Tiempo desde la anomalía hasta la intercepción humana/automática. (Se reduce con Telemetría Observacional).
2.  **MTTR (Mean Time To Resolve)**: Tiempo para mitigar el fallo percibido por el usuario.

**El Teorema del Rollback (Leyenda SRE)**:
No intentes bajar el MTTR depurando el código con hilos colgados a las 3 AM. A las 3 AM tu cerebro opera con un déficit cognitivo severo.
Para llevar el MTTR matemático a valores de milisegundos, la estrategia universal es **el Rollback Ciego e Inmediato** (Revertir al contenedor/imagen Docker de hace 1 hora) o el Failover de DNS/BGP a una región sana (Canary Routing). 
El análisis forense de *Root-Cause* (Postmortem Blameless) se realiza al día siguiente analizando los perfiles de Pprof o *coredumps* bajo estricta luz diurna.
