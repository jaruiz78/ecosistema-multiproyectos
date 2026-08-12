# Módulo 0.3: Algoritmos de Elección de Líder (Leader Election)

---

## 1. 🐣 Rincón Junior: ¿Por qué necesitamos un Jefe?

Si pones a 5 programadores a pintar una pared al mismo tiempo sin hablar entre ellos, probablemente pinten con 5 colores distintos y el resultado sea un desastre.
En un clúster de servidores (ej. Kafka, MongoDB, Kubernetes), si los 5 servidores intentan escribir datos en el mismo disco duro al mismo tiempo, corromperán el archivo. 
Necesitamos que los 5 servidores se reúnan, voten, y digan: *"El Servidor A es el Jefe (Leader). Solo él puede escribir en el disco. Los demás (Followers) haremos copias de seguridad de lo que él diga"*.
Pero, ¿qué pasa si el servidor A se desenchufa o explota? Los 4 seguidores restantes tienen milisegundos para darse cuenta de la muerte de A, y celebrar unas nuevas "elecciones democráticas" sin intervención humana para elegir un nuevo Jefe antes de que los usuarios noten el fallo.

---

## 2. 🔬 Fundamentos Arquitectónicos: Asimetría y Evitación del Split-Brain

La Elección de Líder transforma un sistema distribuido **simétrico** (todos los nodos son iguales) en uno **asimétrico** (un nodo es especial).
Esto simplifica brutalmente la arquitectura matemática del consenso, ya que el Líder actúa como un cuello de botella serializador: todas las peticiones (compras, mensajes) pasan por él, garantizando un orden temporal perfecto sin colisiones.

**El Peligro del Split-Brain (Revisión)**:
Si la red se corta y 2 seguidores no pueden comunicarse con el resto, podrían pensar: *"El Líder ha muerto, yo seré el nuevo Líder"*. Ahora tienes 2 líderes en el mismo clúster escribiendo datos contradictorios. 
Para evitar esto, **todos los algoritmos de Elección de Líder requieren una Mayoría (Quórum)**. En un grupo de 5, necesitas al menos 3 votos para ser líder. Como solo hay 5 votos en total, es matemáticamente imposible que se elijan 2 líderes simultáneos (porque 3 + 3 = 6, lo cual es imposible).

---

## 3. 🚀 Algoritmos Clásicos: El Bully y el Ring

Estos algoritmos se enseñan en Berkeley y el MIT para entender las bases matemáticas, aunque hoy en día se encapsulan en librerías.

### El Algoritmo Bully (El Matón - Garcia-Molina, 1982)
Se asume que cada servidor tiene un ID numérico único (ej. IPs o números del 1 al 5).
La regla de oro: **El nodo vivo con el ID más alto siempre debe ser el líder**.
1. Si el servidor 3 nota que el líder actual (el 5) no responde (Timeout), inicia unas elecciones enviando un mensaje `ELECTION` a todos los nodos superiores a él (al 4 y al 5).
2. Si el 4 está vivo, le responde `OK` al 3 (diciéndole: *"cállate, yo soy mayor que tú, yo me encargo"*), y el 4 envía `ELECTION` al 5.
3. Si el 5 está muerto y no responde al 4, el 4 no recibe respuesta de nadie superior.
4. El 4 se proclama ganador, y envía un mensaje `COORDINATOR` a todos los nodos inferiores (1, 2 y 3). 
*Matemáticamente simple, pero genera mucha redundancia de mensajes $O(N^2)$ si muchos nodos fallan a la vez.*

### El Algoritmo de Anillo (Ring Election)
Los nodos se organizan matemáticamente en un anillo lógico (1 apunta a 2, 2 a 3...).
Si un nodo nota que el líder muere, crea un mensaje con su propio ID y lo pasa al siguiente en el anillo. Cada nodo vivo añade su ID a la lista y lo pasa. Cuando el mensaje da la vuelta completa, el nodo mira la lista, coge el ID más alto, y proclama al ganador.

---

## 4. 🧠 Internals Avanzados: Raft Leader Election (El Estándar Moderno)

Sistemas modernos como etcd (Kubernetes), Consul, y bases de datos modernas usan **Raft** (Diseñado por Stanford, Diego Ongaro, 2014) porque es explícitamente diseñado para ser "Understandable" a diferencia de Paxos.

Raft usa un concepto llamado **Election Timeout Aleatorio** (Jitter).
1. Cuando el clúster arranca, todos son Followers. Todos inician una cuenta regresiva matemática aleatoria entre 150ms y 300ms.
2. El servidor 2 tiene el timeout más corto (ej. 160ms). Su temporizador llega a cero antes que los demás.
3. El servidor 2 cambia su estado a **Candidate**, incrementa el "Term" (Mandato electoral), se vota a sí mismo, y pide votos (`RequestVote`) a los demás.
4. Los demás, como aún no han llegado a cero, y es el primer candidato que ven para este mandato, le dan su voto.
5. El servidor 2 gana el Quórum y se vuelve **Líder**.
6. Inmediatamente, envía mensajes de "Latido de Corazón" (`Heartbeat` / `AppendEntries` vacíos) cada 50ms a los demás.
7. Al recibir los Heartbeats, los seguidores reinician su reloj de 150-300ms. Nunca llegarán a cero mientras el líder esté vivo.

La **matemática aleatoria (Jitter)** es el genio de Raft: evita que dos nodos terminen su cuenta atrás exactamente al mismo milisegundo y empaten en votos constantemente (Split-Vote).

---

## 5. ⚠️ Runbook SRE: Elecciones Interminables (Flapping)

**Incidente**: El clúster de Kubernetes (etcd) deja de aceptar despliegues. Los logs muestran que los nodos están celebrando elecciones de líder continuamente (Election Flapping), cambiando de líder cada 300ms. Ninguno dura lo suficiente para hacer trabajo útil.

**Diagnóstico Arquitectónico**:
El disco duro subyacente (I/O) o la red del clúster está saturada (alta latencia).
El líder envía su Heartbeat de Raft. Pero debido a la saturación del disco (SSD lento o disco en red saturado), el procesamiento de la red en los Followers tarda 400ms. 
Como la cuenta regresiva de los Followers es de 300ms, el temporizador llega a cero *antes* de que procesen el latido del líder. Un Follower asume falsamente que el líder murió, e inicia elecciones, destituyendo al líder válido.

**Solución SRE (Tunear el Heartbeat y Timeout)**:
1.  **Regla Matemática Raft**: `broadcastTime << electionTimeout << MTBF (Mean Time Between Failures)`.
2.  Si la latencia de red/disco de tu nube (broadcastTime) sube de 5ms a 50ms (por ráfagas en la nube), tu `electionTimeout` de 150ms se queda muy corto.
3.  Reconfigurar inmediatamente el cluster (ej. banderas de etcd `--election-timeout=5000` y `--heartbeat-interval=500`). Pierdes unos segundos en detectar una caída real, pero estabilizas matemáticamente el clúster bajo alta carga de red.

---
---

# 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

Expandimos la teoría de Raft más allá del proceso electoral, centrándonos en el mecanismo crítico que hace a Raft valioso en la industria: La Replicación de Logs Consistente (Log Replication) y la anatomía interna de los volcados transaccionales (como los usados en `etcd`).

## 6. Raft Log Replication Internals (La Máquina de Estados)

Una vez que un servidor gana las elecciones mediante Jitter aleatorio, asume el control absoluto del *Replicated State Machine*.
En sistemas como K8s, cualquier cambio de estado (crear un Pod, borrar un Deployment) fluye mediante esta máquina:

1. **Client Request**: El API de K8s recibe `POST /pods`. Se lo manda al Líder de etcd.
2. **Append (Uncommitted)**: El Líder inscribe la instrucción en su Log interno (disco SSD duro). En este punto, la instrucción *no* es visible para los clientes. Sigue siendo *uncommitted*. Cada entrada del Log tiene un `index` y un `term`.
3. **Replicate (AppendEntries)**: El Líder envía RPCs en paralelo (`AppendEntries`) a los Followers, pidiéndoles que escriban exactamente la misma línea en sus logs.
4. **Commitment (Quórum)**: Cuando la **mayoría** matemática (3 de 5 nodos) responde "Sí, ya lo escribí en mi SSD", el Líder marca la entrada como `committed`.
5. **Apply / Execution**: El Líder aplica la mutación a su State Machine en RAM (el árbol b-tree o mapa en memoria).
6. **Notification**: El Líder responde HTTP 200 OK al API Server, indicando que el Pod ha sido guardado exitosamente. En el siguiente Heartbeat, informa a los Followers de que esa entrada ya es oficial (`commitIndex`), para que ellos también la apliquen en RAM.

### Prevención Matemática de Inconsistencias (Log Matching Property)
El teorema central de seguridad de Raft postula que:
> *Si dos logs distintos contienen una entrada con el mismo `index` y el mismo `term`, entonces los logs son idénticos matemáticamente desde el inicio hasta ese índice.*

Para lograr esto, la RPC de `AppendEntries` siempre contiene el `index` y el `term` de la entrada *inmediatamente anterior*. El Follower revisa su propio log: si su entrada anterior no coincide, rechaza la RPC. El Líder entonces retrocede y le re-envía todo el log desde el punto en el que el Follower divergía, sobreescribiendo brutalmente el historial falso del Follower.

## 7. Anatomía Clandestina de `etcd` (Dumps y Estructura WAL)

`etcd` es el corazón de Kubernetes, escrito puramente en Go e implementando Raft. Toda su seguridad descansa en la manera en la que vuelca datos al disco mediante WAL (Write-Ahead Log) *antes* de confirmar red.

### Análisis del WAL (`.wal` file structure)
Si entras mediante SSH a un nodo master de Kubernetes y haces un volcado crudo (hex-dump) del disco en `/var/lib/etcd/member/wal/`, verás registros empacados en protobuf. Cada registro (`walpb.Record`) tiene:
* `Type`: Qué es (Metadata, Entry, State).
* `Crc`: Suma de comprobación de integridad para detectar sectores SSD corruptos (bit-rot).
* `Data`: El byte array del mensaje.

**Ensamblador de Go y Fsync**:
La persistencia real y determinista no se da escribiendo en un buffer de SO, sino invocando el *syscall* directo de hardware para vaciar las memorias caché (page cache flush).

```go
// Fragmento pseudo-código Go inspirado en las entrañas de etcd
func (w *WAL) saveEntry(entry pb.Entry) error {
	data := pb.MustMarshal(&entry)
	rec := walpb.Record{Type: EntryType, Data: data}
	// ... serializa y escribe en memoria ...
	w.encoder.encode(&rec)

	// SYSCALL CRÍTICO: Obliga al SO y al SSD a consolidar electrones.
	// Sin esto, un apagón eléctrico destruiría la asunción de "permanencia" 
	// en la prueba matemática de Raft (Crash-Recovery corrupto).
	err := w.file.Sync() 
	return err
}
```

### Pre-Voting (Prevención SRE Avanzada)
Si el clúster se desconecta y un Follower se queda aislado, su contador `term` subirá infinitamente al convocar elecciones fallidas en solitario (Term = 100, 101, 102...). Al reconectarse, su `term` será astronómicamente alto (103), forzando al Líder legítimo (Term = 4) a renunciar al poder de forma innecesaria.
*Solución implementada en etcd*: La fase `Pre-Vote`. Un nodo aislado primero manda un sondeo (`PreVote`) sin subir su Term. Si no puede contactar a una mayoría, deduce matemáticamente que él es el aislado y *no* altera su mandato ni irrumpe en el clúster al volver.
