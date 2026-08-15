# Módulo 5.4: Infraestructura como Código (IaC) y Terraform

---

## 1. 🐣 Rincón Junior: Los Clics de Ratón Prohibidos

Si tu empresa necesita una Base de Datos, un programador Junior entraría a la web de Google Cloud, pulsaría "Crear Base de Datos", rellenaría un formulario con su ratón y listo.
¿Qué pasa 1 año después? Alguien borra la base de datos por accidente. La empresa entra en pánico. Tienen que volver a entrar a la web, intentar recordar qué casillas marcaron, qué IP le pusieron, qué red usaron. Es imposible de recrear exactamente igual.
Para empresas de nivel SRE (Google, Amazon), **tocar la nube con el ratón (ClickOps) es un despido procedente**.
Todo, absolutamente todo (Bases de datos, servidores, firewalls, alarmas PagerDuty) debe escribirse en un archivo de texto de código fuente (`main.tf`). A esto se le llama **Infraestructura como Código (IaC)**. Si el servidor se quema, ejecutas el código y en 5 minutos tienes un clon exacto (Reproducibilidad Matemática).

---

## 2. 🔬 Fundamentos Arquitectónicos: Declarativo vs Imperativo

**Scripts Imperativos (Bash, Python Antiguo)**: Le dices al ordenador *CÓMO* hacer las cosas, paso a paso.
`if no_existe_bbdd(): crear_bbdd()`
`if nombre_bbdd_es_viejo(): renombrar_bbdd()`
Esto es un infierno. Hay infinitas variaciones del estado actual y el código se llena de `ifs`.

**Terraform (HCL - Declarativo)**: Le dices al ordenador *QUÉ* quieres al final. El fin matemático.
```hcl
resource "google_sql_database_instance" "mi_db" {
  name = "base-de-datos-viajes"
  tier = "db-f1-micro"
}
```
A Terraform no le importa si hoy es lunes o si la base de datos existe. Él compara ese texto con la realidad de GCP.
*   Si no existe, la crea.
*   Si existe y se llama igual, se queda quieto (Idempotencia).
*   Si en la nube alguien le cambió la RAM con el ratón, Terraform lo detecta y la formatea para devolverla al estado matemático que dice el código. **El código es la fuente absoluta de la verdad.**

---

## 3. 🚀 Arquitectura Práctica: El Estado de Terraform (`.tfstate`)

¿Cómo sabe Terraform, que es un programa escrito en Go sin base de datos propia, qué cosas existen en la cuenta gigante de Google Cloud?
Cuando ejecuta un despliegue por primera vez, crea un archivo JSON gigantesco llamado `terraform.tfstate` (El Estado).
Este archivo es un Snapshot (foto) de la realidad. Cada ID interno de Google, cada IP, cada certificado TLS emitido, está guardado ahí.
En el próximo `terraform plan`, la matemática de 3 vías (3-Way Merge) es:
1. `(Código Actual HCL)`
2. `(Archivo de Estado .tfstate Cache)`
3. `(Estado Real en GCP vía API GETs)`

**Peligro Crítico**: Si pierdes el archivo `terraform.tfstate` local (ej. lo borras de tu disco), Terraform sufrirá amnesia. La próxima vez dirá: *"El código me pide una Base de Datos, pero mi .tfstate está vacío. Voy a crear OTRA base de datos desde cero"*, causando un error de `AlreadyExists` en Google Cloud y rompiendo tu pipeline de CD (Continuous Deployment).

---

## 4. 🧠 Internals Avanzados: El Grafo Dirigido Acíclico (DAG) y Ordenamiento Topológico

La magia computacional de Terraform es que tú le pasas 50 archivos `.tf` en desorden, y él sabe exactamente qué crear primero. No ejecuta el código de arriba a abajo. Compila el HCL en un AST (Abstract Syntax Tree) y luego en un **Grafo Dirigido Acíclico (DAG)** de dependencias puras.

### La Matemática del Topological Sort (Algoritmo de Kahn)

Si declaras un Cloud Run ($A$) que recibe la cadena de conexión de una Base de Datos ($B$), y la base de datos reside en una Subred ($C$):
Terraform detecta las referencias implícitas en HCL (ej. `google_sql_database_instance.mi_db.private_ip`) e inserta aristas dirigidas en el Grafo: $A \to B \to C$.

Para ejecutar, Terraform usa el algoritmo de Ordenamiento Topológico para resolver grafos sin ciclos:
1. Encuentra todos los nodos (Vértices) que tienen **In-Degree 0** (No dependen de nadie). Ej. La Subred $C$.
2. Los despacha a ejecución en *Goroutines* paralelas.
3. Una vez creados, elimina sus aristas salientes del grafo.
4. Repite el ciclo iterativamente bajando por la frontera de dependencia.
Si el grafo detecta un ciclo ($A \to B \to C \to A$), el compilador falla matemáticamente en `terraform plan` con el error *Cycle Error*, protegiendo la cuenta de un bucle infinito en infraestructura. Este diseño en Go permite que cientos de recursos aislados se creen concurrentemente ($O(1)$ wall-clock time frente al $O(N)$ secuencial).

---

## 5. ⚠️ Runbook SRE: Remote State Locking y Terraform Drift

**Incidente SRE**: El Programador A (en Madrid) y el Programador B (en Londres) ejecutan `terraform apply` exactamente en el mismo segundo desde sus portátiles sobre el mismo clúster de K8s. Tienen archivos `.tfstate` locales. Se pisan mutuamente (Race Condition) y la infraestructura se corrompe severamente.

**Corrección SRE Estricta (Remote Backend y Lock de Mutex)**:
El `.tfstate` **NUNCA** debe guardarse localmente ni en GitHub (contiene los secretos en texto plano).
1. Se configura un Backend de Estado en Google Cloud Storage (Bucket).
2. Cuando el Programador A ejecuta Terraform, el binario realiza una llamada HTTP atómica a Cloud Storage para adquirir un Lock.
3. El Programador B lanza Terraform 50 milisegundos después, y recibe un *Error 423 Locked*: "El estado está siendo modificado por A. Abortando".

**El Fenómeno Drift (Deriva del Estado)**:
Ocurre cuando un administrador, presa del pánico por una caída, entra por la consola web (ClickOps) y aumenta la RAM de la base de datos.
La realidad $\neq$ El Código.
La mitigación corporativa GitOps estricta consiste en **eliminar los permisos de escritura (IAM Write)** de Google Cloud a todos los humanos. El único ente físico autorizado para mutar recursos de producción es un CI/CD Pipeline robótico (ej. un Runner asilado ejecutando Terraform o Atlantis tras la fusión (merge) de un Pull Request revisado).


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Infraestructura como Código (IaC) y Terraform** a un estudiante de secundaria, **sin usar las palabras:** "Infraestructura", "como", "Código" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
