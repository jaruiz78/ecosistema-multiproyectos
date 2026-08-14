# Módulo 6.6: Estrategias de Despliegue Zero-Downtime (Canary y Blue/Green)

---

## 1. 🐣 Rincón Junior: Cambiando el Motor en Pleno Vuelo

Antiguamente, para actualizar un programa (subir una nueva versión de Java), un SysAdmin ponía un cartel de *"Mantenimiento el domingo a las 3 AM"*. Apagaba el servidor viejo (los clientes perdían servicio), instalaba el código nuevo, y lo encendía.
En el mundo Cloud moderno (Amazon, Netflix, Google), las aplicaciones no se apagan nunca. Se despliegan 500 veces al día. Para lograr esto matemáticamente sin interrumpir las peticiones de los usuarios, usamos técnicas de enrutamiento de red avanzadas. Es como cambiar las ruedas y el motor de un coche mientras va por la autopista a 120 km/h.

---

## 2. 🔬 Fundamentos Arquitectónicos: Blue/Green Deployment

La estrategia **Blue/Green** requiere el doble de dinero e infraestructura durante el despliegue, pero garantiza una ventana de Rollback de latencia $\mathcal{O}(1)$ en la red.

1.  **Entorno Blue (Azul)**: El clúster físico que atiende el `$10`0\%$ de la carga de producción de forma estable ($v_{1.0}$).
2.  **Entorno Green (Verde)**: Un clúster idéntico, aislado, corriendo $v_{2.0}$. Soporta validación de Integración Continua (Tests E2E).
3.  **El Switch Atómico (Router/LB)**: Mediante un cambio de configuración en el Ingress Controller o el DNS/BGP, el puntero (Pointer) de tráfico cambia atómicamente del Azul al Verde.
4.  **Instant Rollback**: Si los umbrales de error (SLI) suben en la versión Verde, el Load Balancer reescribe su tabla de ruteo de vuelta al clúster Azul instantáneamente. El estado defectuoso jamás llega a penetrar profundamente.

---

## 3. 🚀 Arquitectura Práctica: Canary Release y Blast Radius

El problema del Blue/Green es probabilístico: arrojas el `$10`0\%$ de los usuarios a un sistema no probado en el mundo real.
El **Canary Deployment** acota matemáticamente el **Blast Radius** (Radio de Explosión).

1. La Versión $v_{1.0}$ sigue sirviendo la mayoría. Se levantan Pods esporádicos de la Versión $v_{2.0}$.
2. El Enrutador aplica una variable aleatoria uniforme para desviar un subconjunto $p=0.01$ (1%) del tráfico a $v_{2.0}$.
3. **Métricas Estadísticas (Mann-Whitney U Test)**: Los SREs comparan estadísticamente la distribución de latencias de la cohorte $v_{1.0}$ contra la cohorte $v_{2.0}$. Si $v_{2.0}$ degrada el SLO, el Canary es aniquilado. Si supera el umbral de confianza ($p$-value aceptable), la partición aumenta topológicamente al $5\%$, `$1`0\%$, `$5`0\%$ y `$10`0\%$.

---

## 4. 🧠 Internals Avanzados: Istio Envoy y Traffic Shifting Math

En plataformas nativas de Kubernetes avanzadas (Service Mesh), el balanceo no se hace en un componente perimetral lejano, sino a nivel de Pod usando el proxy L7 **Envoy** (Sidecar pattern implementado por Istio).

### La Matemática del Traffic Shifting
Cuando configuras en Istio un `VirtualService` con pesos:
```yaml
route:
- destination: { host: mi-servicio, subset: v1 }
  weight: 90
- destination: { host: mi-servicio, subset: v2 }
  weight: 10
```
Envoy proxy implementa internamente el Algoritmo de **EDF (Earliest Deadline First) para Scheduling Probabilístico**, o usa funciones Hash sobre metadatos HTTP para garantizar consistencia.

1. **Sticky Sessions (Hash Ring)**: Si deseas que el usuario afectado por el Canary 10% no cambie entre versiones y rompa su estado local, Envoy calcula un $H(x) = \text{Hash}(\text{HTTP\_Header\_User\_ID}) \pmod{100}$. Si $H(x) < 10$, el usuario queda matemáticamente anclado (pinned) a $v_{2.0}$ durante todas sus interacciones, garantizando un A/B testing determinista sin corromper sus transacciones asíncronas.
2. Esta lógica descentralizada de Envoy ocurre en $<1ms$ en el espacio del contenedor, permitiendo Canary Deployments masivos y concurrentes con coste de red y CPU insignificantes.

---

## 5. ⚠️ Runbook SRE: Database Schema Evolution Locks (SPOF)

**Incidente SRE**: Se despliega una versión Canary (10%) de un worker en Go que incluye una migración Liquibase. El script renombra topológicamente la columna `coste_euros` a `tarifa_total`. El despliegue Canary arranca, aplica el ALTER TABLE a PostgreSQL (Mutex exclusivo), y repentinamente el `$9`0\%$ de los contenedores $v_{1.0}$ sufren colapso fatal (Crash Loop) al intentar leer la columna extinta.

**Diagnóstico Arquitectónico (State Coupling)**:
El código (Stateless) soporta bifurcación Canary en paralelo, pero el Estado de Base de Datos (Stateful) es un Punto Único de Fallo Estructural. Una tabla SQL no puede estar en dos estados topológicos a la vez.

**Solución SRE Rigurosa (Patrón Expand and Contract)**:
Las mutaciones de esquema acopladas al código deben romper su sincronicidad en 4 fases matemáticas:
1.  **Fase 1 (Expand)**: Modificar el esquema (ADD COLUMN `tarifa_total`), manteniendo la vieja `coste_euros` intacta.
2.  **Fase 2 (Canary del Código)**: Subir $v_{2.0}$. Su código implementa un **Dual-Write**: escribe asíncronamente en ambas columnas y confía preferentemente en `tarifa_total`. El `$9`0\%$ de $v_{1.0}$ sigue operando sanamente sobre la columna vieja.
3.  **Fase 3 (Migración de Datos)**: Un backfill job copia en lote (ETL) los datos antiguos de la columna vieja a la nueva.
4.  **Fase 4 (Contract)**: Una vez que el `$10`0\%$ del tráfico está en $v_{2.0}$, se emite un despliegue final $v_{3.0}$ que elimina la lectura de la columna vieja, y se ejecuta el `DROP COLUMN`. Se previene la destrucción catastrófica del estado garantizando Compatibilidad hacia Atrás y hacia Adelante.
