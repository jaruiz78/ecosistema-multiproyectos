# Módulo 6.8: Respuesta a Incidentes y Postmortems (Blameless Culture)

---

## 1. 🐣 Rincón Junior: ¿De Quién es la Culpa?

El servidor ha caído, perdiendo 10,000€ por minuto. Un programador Junior introdujo un comando `DROP TABLE` por accidente porque los servidores de Producción y Desarrollo tenían el mismo color en su terminal.
El CEO grita: *"¡Despedid al culpable!"*.
**SRE de Google rechaza esto radicalmente.** Despedir al Junior no soluciona el problema. Si él cometió el error, el sistema le permitió cometerlo. La culpa no es humana, la culpa es arquitectónica. Si despides al humano, el próximo programador cometerá el mismo error.
El núcleo de la cultura SRE es el **Postmortem Blameless (Libre de Culpa)**: Asumir matemáticamente que todos los operadores tienen buenas intenciones y el fallo reside en la falta de resiliencia del sistema sociotécnico.

---

## 2. 🔬 Fundamentos Organizacionales: Incident Command System (ICS)

Cuando ocurre una catástrofe sistémica (SEV-1 / Outage), un sistema distribuido no puede ser mitigado por 30 ingenieros gritando anárquicamente en un canal de Slack. SRE adopta el modelo del Cuerpo de Bomberos y agencias federales (NIMS): El Sistema de Comando de Incidentes.
Roles ortogonales obligatorios:

1.  **Incident Commander (IC)**: El orquestador algorítmico. **NO toca el código, NO mira métricas, NO arregla el problema**. Su única función es mantener el modelo mental de la crisis, asignar tareas, controlar el tiempo (Timeboxing) y tomar la decisión ejecutiva final (ej. "Autorizo el Failover a Tokio, asumiendo la pérdida de los últimos 5 minutos de datos"). Lo que dicta el IC es axiomático.
2.  **Operations Lead (Ops)**: El ingeniero técnico que manipula los sistemas (Terminal, Dashboards, CI/CD). Traduce el mandato del IC en Syscalls y comandos mutadores.
3.  **Communications Lead (Comms)**: Actúa como barrera de aislamiento térmico. Redacta comunicados periódicos a ejecutivos y clientes, bloqueando interrupciones directas al IC y a Ops para mantener su "Flow State" inquebrantable.

---

## 3. 🚀 Arquitectura Práctica: Resiliencia Sociotécnica y el Postmortem

El software no es código matemático puro; es ejecutado e intervenido por humanos bajo estrés. Esto es un **Sistema Sociotécnico**. Si un servidor se reinicia sin generar conocimiento para la organización, el incidente ha sido un fracaso.
Un incidente no termina cuando se recupera el SLI; termina cuando el Postmortem se firma (típicamente $<48$ horas post-incidente).

**Estructura Taxonómica del Postmortem**:
*   **Contexto y Síntomas**: Qué sintió el usuario final y cuándo.
*   **Timeline (Cronología de Precisión)**: Registro histórico de la derivada del incidente (ej. `10:04:02 UTC - Salta alarma Burn Rate. 10:15:00 - IC asume el mando. 10:45:00 - Rollback completado`).
*   **Impacto Real**: Matemáticas puras (ej. "345,000 peticiones 500 arrojadas, $0.01\%$ de pérdida sobre SLA. 15,000$ impacto financiero estimado").
*   **Root Cause (Causa Raíz)**: Desglose sistémico de las vulnerabilidades subyacentes.
*   **Action Items (Remediaciones)**: Funciones forzantes a nivel de código (Tickets de Jira) para que la vulnerabilidad sea físicamente imposible de repetir (Evitar el mismo Root Cause).

---

## 4. 🧠 Internals Avanzados: Ishikawa y la Técnica de los 5 Porqués

Para extraer la Causa Raíz (Root Cause) de la topología de un accidente, la heurística SRE adopta la técnica de los **5 Whys (Toyota)** o Diagramas de Ishikawa (Fishbone). El objetivo es cruzar las fronteras de abstracción desde el fallo físico hasta la falla en los procesos de la empresa.

*   **Problema (Síntoma)**: El clúster de Base de Datos principal se apagó en producción (OOM Killed).
*   1. *¿Por qué?* Porque el File System del nodo líder llegó al 100% de ocupación.
*   2. *¿Por qué?* Porque un servicio Sidecar empezó a escribir logs de debug a 5GB/s en un bucle infinito.
*   3. *¿Por qué?* Porque el servicio de logs no tenía configurada una política determinista de rotación (Log Rotation max-size).
*   4. *¿Por qué?* Porque el pipeline de CI/CD (Terraform DAG) no incluía la variable obligatoria para la cuota de retención, inyectando el valor por defecto infinito.
*   5. *¿Por qué?* Porque los Pull Requests de Infraestructura (IaC) no tienen Linters estáticos (OPA / Checkov) bloqueantes antes del Merge.

**Resultado SRE (Remediación)**: La causa no es el "Disco Lleno". La Causa Raíz Sistémica es la **ausencia de Linters en IaC**. El Action Item es implementar Open Policy Agent (OPA) en las Actions de GitHub. 

---

## 5. ⚠️ Runbook SRE: Normalización de la Desviación y Alert Fatigue

**Incidente (El Lobo y el Pastor)**: A las 3 AM suena el PagerDuty del SRE de guardia: "Pico de Latencia en Worker C". El ingeniero, agotado, silencia la alarma asumiendo que "eso salta todas las madrugadas y se arregla solo". A las 6 AM, la plataforma colapsa irreversiblemente (Cascading Failure silencioso).

**Diagnóstico Sociotécnico (Normalización de la Desviación)**:
Analizado por Diane Vaughan durante el desastre del Challenger de la NASA: Cuando un sistema genera constantemente alertas anómalas que no terminan en catástrofe, la psique humana ajusta su modelo base de "normalidad" integrando el fallo. La anomalía se racionaliza y se acepta como rutina (Fatiga de Alertas).

**Solución SRE Fundamental (Alert Pruning)**:
1. **Borrado Despiadado (Pruning)**: Si una alarma se enciende y un humano no requiere ejecutar una acción física/teclado en $<15$ minutos para prevenir un daño inminente, **la alerta debe ser aniquilada del sistema PagerDuty** y relegada a un log analítico en frío.
2. **Alertas Basadas en SLOs (Burn Rate)**: Ninguna alerta debe basarse en umbrales estáticos del hardware ("CPU > 90%"). Solo debe sonar el teléfono de guardia a las 3 AM si el presupuesto de errores ($MWW \text{ Burn Rate}$) indica que el cliente final está sufriendo un dolor probabilísticamente insoportable y el SLO legal está a horas de ser violado.
