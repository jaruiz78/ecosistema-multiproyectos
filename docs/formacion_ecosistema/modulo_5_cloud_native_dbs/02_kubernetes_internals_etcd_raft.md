# Módulo 5.2: Internals de Kubernetes (K8s), etcd y Control Plane

---

## 1. 🐣 Rincón Junior: El Director de Orquesta

Si tienes 1 contenedor Docker corriendo tu web, puedes gestionarlo tú mismo entrando por SSH y tecleando comandos.
Pero, ¿qué pasa si el Viernes Negro (Black Friday) tu web recibe 100,000 visitas por minuto y necesitas **arrancar 500 contenedores de repente**, repartidos en 30 ordenadores físicos distintos, y además configurar los firewalls de red para que se vean entre sí? Un humano no puede hacer eso.
**Kubernetes (K8s)**, creado por Google (basado en su sistema interno *Borg*), es un Director de Orquesta robótico. Tú no le dices a Kubernetes *cómo* hacer las cosas. Le entregas un papel (un archivo YAML) que dice: *"Quiero que SIEMPRE haya 5 contenedores de mi web corriendo"*. K8s se encarga mágicamente de vigilar, arrancar, apagar y reconectar ordenadores físicos para cumplir tu deseo.

---

## 2. 🔬 Fundamentos Arquitectónicos: Control Plane vs Worker Nodes

Kubernetes divide su clúster rígidamente en dos castas:

1.  **El Plano de Control (Control Plane / Máster)**: Es el cerebro. Nunca ejecuta tus programas web. Solo toma decisiones matemáticas.
2.  **Los Nodos Trabajadores (Worker Nodes)**: Son los ordenadores "tontos" que hacen el trabajo pesado, ejecutando tus contenedores de Spring Boot o Go (Agrupados en **Pods**).

### La Anatomía del Cerebro (Control Plane):
*   **kube-apiserver**: El único componente que tiene un puerto abierto al mundo exterior. Todo, absolutamente todo (tú, tus scripts CI/CD, los demás componentes) tiene que hablar con él vía REST JSON.
*   **kube-scheduler**: El organizador matemático. Cuando el API dice "Hay que arrancar un Pod nuevo", el Scheduler mira los 30 nodos, evalúa su CPU, su RAM, y sus reglas geográficas (Afinidad), y toma la decisión óptima: *"Ponlo en el Nodo 12"*.
*   **kube-controller-manager**: Es un bucle infinito en código Go (Reconciliation Loop). 10 veces por segundo, comprueba el Estado Deseado (YAML: 5 Pods) contra el Estado Actual (Realidad: 4 Pods). Si hay diferencia, manda una orden matemática para corregir la realidad (arrancar 1 Pod).

---

## 3. 🚀 Arquitectura Práctica: Kubelet y Kube-Proxy

¿Qué hay dentro de los ordenadores Trabajadores (Worker Nodes)?
*   **Kubelet**: Es el agente del FBI de Kubernetes infiltrado en cada ordenador. Escucha órdenes del `kube-apiserver`. Cuando recibe la orden *"Arranca el Contenedor de Spring Boot"*, el Kubelet llama al Kernel de Linux (`containerd`) y ejecuta el chroot/cgroups. Si el contenedor se cuelga (Crash), el Kubelet lo reinicia automáticamente en milisegundos.
*   **Kube-Proxy**: Es el mago de la red. Modifica mágicamente las reglas del firewall matemático interno de Linux (`iptables` o `IPVS`). Gracias al Kube-Proxy, si el Contenedor A quiere hablar con el Contenedor B, no necesita saber su IP real cambiante, solo usa un nombre de dominio DNS falso (ej. `http://mi-microservicio-b`).

---

## 4. 🧠 Internals Avanzados: `etcd` y el Patrón Estado Declarativo

El `kube-apiserver` es tonto, no tiene memoria propia. Toda la memoria del cerebro de Kubernetes (el estado de todos los nodos, passwords, IPs y secretos) está guardada en **etcd**.

`etcd` es una base de datos Key-Value distribuida (como Redis, pero hiper-persistente). Es el corazón absoluto del Gemelo Digital.
**¿Por qué etcd es tan especial?**
1.  **Algoritmo Raft (Consenso)**: Usa el algoritmo matemático Raft (visto en el Módulo 0.3) para ser tolerante a fallos. Funciona en números impares (3 o 5 servidores etcd). Si 2 servidores de etcd arden, el sistema K8s sigue al 100% operativo porque el Quórum se mantiene intacto. Si etcd muere completo, Kubernetes se congela: los contenedores vivos siguen vivos, pero es imposible crear o borrar nada nuevo.
2.  **Sistema Watch (Notificaciones Inversas)**: A diferencia de MySQL, donde el código tiene que hacer `SELECT` cada 5 segundos para ver si algo ha cambiado, etcd soporta el comando `WATCH`. El `kube-apiserver` se inscribe matemáticamente en etcd. Cuando un humano lanza un YAML modificando algo, etcd *empuja* activamente el cambio al API Server, que avisa a los Controladores en microsegundos, creando un sistema puramente Reactivo y Event-Driven, capaz de gestionar decenas de miles de servidores sin consumir CPU inútilmente con bucles de espera.

---

## 5. ⚠️ Runbook SRE: Split-Brain y Split de etcd

**Regla estricta SRE de Kubernetes**: etcd es increíblemente sensible a la latencia de disco (IOPS) y de red.
Si instalas los 3 nodos de etcd en 3 regiones geográficas (Madrid, Tokio, Nueva York) para ser "muy seguro", el milisegundo de Round-Trip de la luz a través del mundo destrozará el `Election Timeout` matemático del algoritmo Raft.
El clúster etcd entrará en continuas Elecciones de Líder (Leader Flapping), provocando que todo tu Kubernetes deje de aceptar órdenes (Timeouts al desplegar apps).
*Mejor práctica SRE*: etcd siempre debe estar en la **misma Región** de GCP (ej. 3 Zonas de Disponibilidad en `europe-southwest1`), garantizando latencias $\leq 1$ milisegundo para los Heartbeats de Raft.

---
---

# 🛑 [DEEP-DIVE] API Server Watch Mechanisms y Patrón Informer

Para perfiles SRE, Desarrolladores Go de Custom Controllers y Arquitectos de Plataforma, el verdadero milagro de rendimiento de K8s reside en cómo la librería `client-go` implementa la sincronización distribuida usando **Informers**, **Reflectors** y colas indexadas.

## 6. La Arquitectura Push (Watch vs Polling)

Un clúster Kubernetes empresarial puede tener 100,000 Pods. Si el `kube-controller-manager` hiciera una petición HTTP REST tradicional (`GET /api/v1/pods`) cada 1 segundo, colapsaría la red, el `kube-apiserver` y reventaría la base de datos `etcd`.

Para solucionar esto, Kubernetes utiliza el protocolo **HTTP/1.1 Chunked Transfer Encoding** o **HTTP/2 Streams** combinado con la capacidad primitiva `WATCH` de `etcd` (usando gRPC).
1. El Controller (ej. ReplicaSetController) abre una conexión HTTP infinita y permanente hacia el API Server `GET /api/v1/pods?watch=true`.
2. El API Server delega este Watch a `etcd`.
3. Cuando un Nodo Worker se cae, `etcd` recibe la mutación `Pod.status=Failed`.
4. `etcd` empuja este Delta vía gRPC al API Server.
5. El API Server serializa el evento (Added, Modified, Deleted) a JSON y lo escupe por la conexión HTTP infinita del Controller.
*Resultado*: Complejidad de Red $O(1)$ evento por cambio, 0% CPU en tiempo inactivo.

## 7. Informers, Reflectors y la Local Store (SharedInformer)

Escribir código Go robusto para escuchar un HTTP Stream infinito es un infierno (desconexiones, reintentos, ResourceVersions perdidas). La librería `client-go` expone el patrón **SharedInformerFactory** para encapsular esta complejidad.

```go
// Pseudocódigo arquitectónico del patrón Informer de Kubernetes en Go
import (
    "k8s.io/client-go/informers"
    "k8s.io/client-go/tools/cache"
)

func main() {
    // 1. Shared Informer: Todos los controllers de este binario comparten UNA sola conexión HTTP.
    informerFactory := informers.NewSharedInformerFactory(clientset, time.Minute*10)
    
    // 2. Reflector: Conecta al API Server, hace un LIST (full sync) y luego un WATCH continuo.
    podInformer := informerFactory.Core().V1().Pods().Informer()

    // 3. Local Store (Thread-safe cache): El Reflector vuelca los eventos aquí.
    // Garantiza que lecturas locales cuesten O(1) memoria sin golpear la red.
    lister := informerFactory.Core().V1().Pods().Lister()
    
    // 4. Event Handlers: Callbacks reactivos inyectados a la Workqueue
    podInformer.AddEventHandler(cache.ResourceEventHandlerFuncs{
        AddFunc: func(obj interface{}) {
            // Un Pod ha nacido. Meter a la Workqueue (Rate-limited, deduplicada)
            workqueue.Add(obj) 
        },
        UpdateFunc: func(oldObj, newObj interface{}) {
            // Un Pod ha cambiado de estado
            workqueue.Add(newObj)
        },
    })
    
    informerFactory.Start(wait.NeverStop)
}
```

### Arquitectura de Caché Estricta SRE
Como regla de hierro impuesta por Google y el proyecto Kubernetes:
**Los Controllers y el Kube-Scheduler NUNCA leen directamente del API Server (`clientset.CoreV1().Pods().List(...)`).**
En su lugar, inician un *SharedInformer*, que sincroniza asincrónicamente el estado del clúster en una memoria caché local del proceso de Go (Lister). Todos los bucles de reconciliación matemáticos en K8s se resuelven cruzando memoria local contra memoria local, evitando absolutamente latencias de red o bloqueos de transacciones hacia la base de datos `etcd`.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Internals de Kubernetes (K8s), etcd y Control Plane** a un estudiante de secundaria, **sin usar las palabras:** "Internals", "de", "Kubernetes" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
