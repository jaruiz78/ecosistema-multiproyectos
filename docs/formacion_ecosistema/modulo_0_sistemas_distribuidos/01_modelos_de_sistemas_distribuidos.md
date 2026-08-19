# Módulo 0.1: Fundamentos y Modelos de Sistemas Distribuidos (UC Berkeley)

---

## 1. 🐣 Rincón Junior: El Ordenador Partido en Dos

Cuando escribes código en tu portátil (un sistema centralizado), si llamas a la función `guardarUsuario()`, sabes que la memoria RAM está ahí, el procesador está ahí, y si el portátil se apaga, todo muere a la vez. Es binario: o funciona todo, o no funciona nada.
Un Sistema Distribuido es como partir tu portátil por la mitad y poner una mitad en Madrid y otra en Tokio, conectadas por un cable larguísimo.
Ahora surge el terror: **Los Fallos Parciales**. La mitad de Madrid puede estar funcionando perfectamente, enviar un mensaje, y la mitad de Tokio puede haberse derretido. La mitad de Madrid se queda esperando eternamente. ¿El mensaje llegó? ¿Se perdió en el cable? ¿Tokio murió justo después de procesarlo pero antes de responder? **Nunca puedes estar matemáticamente seguro**.

---

## 2. 🔬 Fundamentos Teóricos: El Problema de los Dos Generales

Este es el experimento mental fundacional de las redes de ordenadores.
Dos ejércitos aliados están en montañas separadas, con el enemigo en el valle. Solo pueden ganar si atacan exactamente a la misma hora (las 12:00). Si ataca uno solo, es masacrado.
El General 1 envía un mensajero: *"Atacamos a las 12, ¿confirmas?"*.
El mensajero debe cruzar el valle (Internet) lleno de enemigos (routers que pierden paquetes de red). 
Si el General 2 lo recibe, envía a otro mensajero: *"Confirmado"*.
Pero el General 2 piensa: *"Si mi mensajero muere en el camino, el General 1 no atacará, y si yo ataco, moriré. No puedo atacar hasta que el General 1 me confirme que recibió mi confirmación"*.
El General 1 responde: *"Recibí tu confirmación"*. Pero piensa lo mismo: *"Si este mensajero muere..."*.
**Teorema Matemático**: En una red asíncrona donde los mensajes pueden perderse, es **imposible** lograr consenso absoluto garantizado. Todo sistema distribuido (TCP, Paxos, Raft) es una aproximación estadística para mitigar este teorema sin poder resolverlo al 100%.

---

## 3. 🚀 Arquitectura Práctica: Tipos de Modelos de Red

Para diseñar algoritmos (como las bases de datos de Google), los ingenieros de Berkeley y el MIT modelan matemáticamente la red en dos extremos:

1.  **Modelo Síncrono**: Asume que hay un límite de tiempo estricto garantizado. (ej. "Los mensajes tardan MÁXIMO 2 segundos en llegar"). Si el mensaje no llega en 2 segundos, el otro nodo está muerto 100% seguro. *Problema: Internet real no funciona así.*
2.  **Modelo Asíncrono**: Asume que los mensajes pueden tardar **infinito**. Un retraso de 10 minutos puede ser por red congestionada, o porque el nodo está muerto. Es matemáticamente imposible distinguir entre "nodo muerto" y "nodo lento". Aquí rige el **Teorema FLP**, que demuestra que en este modelo, ningún algoritmo determinista puede lograr consenso si un solo nodo falla.
3.  **Modelo Parcialmente Síncrono (El Mundo Real)**: Asume que la red es asíncrona casi siempre, pero de vez en cuando hay "períodos de buena salud" síncronos. Algoritmos como Paxos y Raft se diseñan para ser *seguros* (nunca corromper datos) durante el caos asíncrono, y *avanzar* (Liveness) durante los períodos síncronos.

---

## 4. 🧠 Internals Avanzados: Clasificación de Fallos (De Crash-Stop a Bizantinos)

No todos los servidores mueren igual. Clasificamos los fallos del más fácil al más apocalíptico:

1.  **Crash-Stop (Fallo por Parada)**: El servidor A se apaga (se queda sin luz). Deja de enviar mensajes para siempre. Es el fallo más fácil de manejar matemáticamente.
2.  **Crash-Recovery**: El servidor A se apaga, y 10 minutos después se enciende. El problema es que ha perdido su memoria RAM y despierta con "amnesia", enviando mensajes contradictorios respecto al pasado si no guardó su estado en disco de forma transaccional.
3.  **Omission (Omisión de Red)**: El servidor A está vivo y sano, pero el router le corta el cable. Envía mensajes pero nunca llegan al servidor B.
4.  **Fallo Bizantino (Byzantine Fault)**: La peor pesadilla matemática. El servidor A no solo falla, sino que actúa con **malicia inteligente o corrupción cósmica**. Debido a un rayo cósmico en la RAM, un hackeo o un bug de memoria de C++, el servidor A envía mensajes perfectamente formateados pero con mentiras (ej. "El saldo es 1000€" al nodo B, y "El saldo es 0€" al nodo C).
    *   *Solución BFT (Byzantine Fault Tolerance)*: Requiere que $3F + 1$ nodos existan para tolerar $F$ nodos traidores (ej. para tolerar 1 servidor hackeado, necesitas 4 servidores totales). Es la base del Blockchain (Bitcoin/Ethereum). En sistemas corporativos internos (Google/Amazon), solemos ignorar los fallos Bizantinos por ser demasiado caros de prevenir, asumiendo que el datacenter es seguro.

---

## 5. ⚠️ Runbook SRE: Split-Brain (Cerebro Dividido)

**Incidente**: Tienes un clúster de Base de Datos Maestro-Esclavo. El Maestro está en Europa, el Esclavo en América. El cable submarino del Atlántico se corta (Omission Fault). 
Europa no puede ver a América. América asume que el Maestro de Europa ha muerto (porque el modelo es asíncrono y confunde lentitud con muerte). América promociona a su Esclavo a nuevo Maestro.
Ahora tienes **Dos Maestros activos**. Los usuarios europeos escriben compras en la base de datos de Europa, y los americanos en América. 
4 horas después, el cable submarino se repara. Ambas bases de datos se sincronizan y los datos colisionan masivamente, corrompiendo financieramente la empresa sin solución.

**Prevención SRE Estricta (Quórum / STONITH)**:
*   Nunca usar topologías de 2 nodos (`N=2`). Siempre usar `N=3`, `N=5`, etc.
*   Si se rompe el cable, el lado con la mayoría matemática (Quórum, ej. 2 nodos) sigue funcionando. El lado con la minoría (1 nodo) entra en estado de pánico y se auto-destruye en modo lectura, previniendo el Split-Brain absoluto.

---
---

# 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

Esta sección expande el conocimiento fundacional a nivel algorítmico y matemático, enfocado para arquitectos que diseñan motores de bases de datos desde cero.

## 6. Demostración del Límite Bizantino ($3F + 1$)

Para comprender por qué necesitamos 4 nodos para tolerar 1 nodo malicioso (Bizantino), analizamos la matemática de intersección de quórums formulada por Leslie Lamport, Robert Shostak y Marshall Pease (1982).

Sea $N$ el número total de nodos. Sea $F$ el número máximo de nodos traidores (Bizantinos).
Para llegar a un consenso, un comandante leal necesita recolectar votos.

1. Al esperar respuestas, el comandante no puede esperar las $N$ respuestas completas, porque los $F$ traidores podrían simplemente quedarse en silencio (Crash-Fault disfrazado), bloqueando el sistema para siempre. Por tanto, el comandante solo puede esperar como máximo $N - F$ respuestas antes de tomar una decisión.
2. De las $N - F$ respuestas recibidas, en el peor de los casos, los $F$ traidores sí han respondido enviando mentiras, y los $F$ nodos honestos que faltan fueron los que se retrasaron por red (asincronía).
3. Para que la mayoría de los votos recibidos provenga matemáticamente de nodos honestos y no de traidores, necesitamos que el número de nodos honestos en ese grupo ($N - 2F$) sea estrictamente mayor que el número de traidores ($F$):
   $N - 2F > F \implies N > 3F \implies N \ge 3F + 1$

**Consecuencia arquitectónica:** Si implementamos PBFT (Practical Byzantine Fault Tolerance) en un consorcio privado de bancos corporativos, y queremos sobrevivir a que 2 bancos sean hackeados, la red *debe* tener al menos $3(2) + 1 = 7$ servidores.

## 7. Teorema FLP (Fischer, Lynch, Paterson - 1985)

El Teorema FLP demuestra formalmente que en un sistema **completamente asíncrono** (donde no hay cota superior para los retrasos de los mensajes o el procesamiento), **no existe ningún algoritmo determinista** que pueda garantizar consenso si tan solo **un nodo** puede fallar (crash-stop).

**Proof Intuición:**
- Un sistema se modela como una máquina de estados global. Existen estados *bivalentes* (donde la decisión final puede ser 0 o 1) y estados *univalentes* (donde la decisión ya está fijada irremediablemente a 0 o 1).
- FLP demuestra que siempre existe un escenario de encolamiento de red (un "demonio" que retrasa los mensajes de forma perversa) que puede mantener al sistema rebotando entre estados bivalentes infinitamente.
- *Solución de la Industria:* Rompemos el determinismo (añadiendo aleatoriedad, como el *randomized timeout* de Raft) o rompemos la asincronía asumiendo cotas parciales de tiempo (Partial Synchrony de Dwork, Lynch, y Stockmeyer).

## 8. Simulador en Go: Fallo Bizantino Práctico (The Byzantine Generals Problem)

Para materializar el fallo, veamos un código en Go (CSP) donde un comandante (General) envía órdenes de "Atacar" o "Retirada", pero uno de los tenientes es Bizantino y miente a los demás sobre lo que el General dijo.

```go
package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Order string
const (
	Attack  Order = "ATTACK"
	Retreat Order = "RETREAT"
)

type Node struct {
	ID        int
	IsTraitor bool
	OrdersReceived []Order
}

func (n *Node) ReceiveOrder(order Order, from int) {
	// Un traidor podría alterar lo que retransmite después
	n.OrdersReceived = append(n.OrdersReceived, order)
}

func main() {
	// Sistema N=3, F=1 (Teorema indica que N=3F+1 no se cumple, 3 < 4). 
    // Por tanto, el consenso fallará miserablemente.
	general := &Node{ID: 0, IsTraitor: false} // Comandante leal
	lieutenant1 := &Node{ID: 1, IsTraitor: false} // Leal
	lieutenant2 := &Node{ID: 2, IsTraitor: true}  // Traidor (Bizantino)

	// Ronda 1: General envía orden
	trueOrder := Attack
	fmt.Println("General (Leal) ordena:", trueOrder)
	lieutenant1.ReceiveOrder(trueOrder, general.ID)
	lieutenant2.ReceiveOrder(trueOrder, general.ID) // El traidor recibe "Attack"

	// Ronda 2: Intercambio entre tenientes (Gossip)
	// El Teniente 1 (Leal) repite la verdad
	lieutenant2.ReceiveOrder(trueOrder, lieutenant1.ID)

	// El Teniente 2 (Traidor) miente deliberadamente al Teniente 1
	maliciousOrder := Retreat
	fmt.Printf("Teniente 2 (Traidor) reporta al Teniente 1 que el General dijo: %s\n", maliciousOrder)
	lieutenant1.ReceiveOrder(maliciousOrder, lieutenant2.ID)

	// Decisión Local (Majority Vote)
	fmt.Printf("Teniente 1 tiene los votos: %v\n", lieutenant1.OrdersReceived)
	// El Teniente 1 tiene [Attack, Retreat]. Empate matemático. 
	// No sabe a quién creer. El consenso determinista se ha destruido con un solo traidor.
}
```

Este pequeño *snippet* ilustra el colapso del consenso determinista bajo $N=3$ con $F=1$. Al ejecutar el código, el Teniente 1 recibe la orden contradictoria sin mecanismo criptográfico para validar la firma del Comandante original (lo que llevaría al modelo de Firmas Infranqueables de Pease, Shostak, Lamport, $SM(m)$).

> [!TIP]
> **Takeaway Arquitectónico**
> En arquitecturas Cloud-Native Corporativas, **asumimos Fail-Stop**, no Fail-Byzantine. Es la única forma de escalar clusters masivos de etcd/Consul manteniendo baja latencia. El cifrado TLS y los VPCs se encargan de mitigar a nivel de red la "malicia bizantina externa".


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Fundamentos y Modelos de Sistemas Distribuidos (UC Berkeley)** a un estudiante de secundaria, **sin usar las palabras:** "Fundamentos", "y", "Modelos" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.

---

```mermaid
flowchart LR
    A["Iniciación / Entrada de Datos"] --> B["Procesamiento en Primeros Principios"]
    B --> C["Garantía Invariante / Rigor Formal"]
    C --> D["Mdulo 01 Fundamentos y Modelos de Sistem: Salida en O(1)"]
```

