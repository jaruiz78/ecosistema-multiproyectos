# Módulo 5.1: Contenedores, Linux Cgroups y Namespaces

---

## 1. 🐣 Rincón Junior: "Funciona en mi máquina"

Hace 15 años, un programador Junior creaba una web en Java 8 en su portátil con Windows. Al pasarla al servidor de Producción (Linux con Java 7), explotaba. El programador decía: *"Pues funciona en mi máquina"*.
La solución antigua era crear Máquinas Virtuales (VMs, ej. VMware o VirtualBox). Una VM copia un disco duro entero, con Windows, drivers de teclado y tarjeta de red falsos, pesando 40 GB y tardando 3 minutos en encender. Era carísimo.
**Docker** y los contenedores solucionaron esto empaquetando el código *junto con todas sus dependencias exactas* en una caja ligera (50 MB) que arranca en 0.1 segundos. Pero hay un secreto: **Los contenedores no existen. Son una mentira.**

---

## 2. 🔬 Fundamentos Arquitectónicos: La Mentira de Docker

Docker no es una tecnología real a nivel de Hardware como las Máquinas Virtuales. Un contenedor es simplemente **un proceso de Linux normal y corriente** (como la calculadora o el navegador web) al que el Sistema Operativo le ha puesto unas "gafas de realidad virtual" y unas "esposas".

Estas gafas y esposas son tres tecnologías primitivas del Kernel de Linux:
1.  **chroot (Change Root)**: Le hace creer al proceso que una carpeta pequeña (`/home/caja/`) es en realidad el directorio raíz absoluto del ordenador (`/`). El proceso no puede ver ni tocar nada fuera de esa caja, porque matemáticamente su "Universo" termina ahí.
2.  **Namespaces (Espacios de Nombres)**: Son las gafas de realidad virtual. Aíslan los recursos abstractos. 
    *   *PID Namespace*: El proceso se ve a sí mismo como el PID 1 (El Dios del sistema), cuando en realidad en el ordenador físico es el PID 4590. 
    *   *Network Namespace*: Le da una tarjeta de red virtual falsa. El proceso cree que es el único en el mundo, no ve a los otros contenedores.
3.  **Cgroups (Control Groups)**: Son las esposas. El Kernel de Linux pone un límite físico matemático. "Tú, proceso 4590, solo puedes usar el 10% de la CPU y 500 MB de RAM". Si el contenedor tiene una fuga de memoria e intenta usar 501 MB, Linux saca el martillo OOM-Killer (Out-Of-Memory) y lo asesina instantáneamente para proteger al resto del servidor.

---

## 3. 🚀 Arquitectura Práctica: OverlayFS (Union File Systems)

Si tengo 100 contenedores de Ubuntu de 100 MB cada uno corriendo en mi servidor, ¿necesito 10 GB de disco duro? No.
Los contenedores usan **OverlayFS**, una maravilla matemática de estructuración de archivos.
Una Imagen de Docker (el empaquetado) está formada por **Capas de solo lectura (Read-Only Layers)** apiladas como piezas de Lego.
*   Capa 1: Ubuntu OS (50 MB).
*   Capa 2: Java 25 (30 MB).
*   Capa 3: Tu código de la App (5 MB).

Si arrancas 100 contenedores, los 100 leen de las **mismas** capas físicas en el disco duro. Solo ocupan 85 MB en total.
¿Pero qué pasa si el Contenedor #4 quiere escribir un archivo temporal (Modificar)?
OverlayFS usa la técnica **Copy-On-Write (COW)**. Crea una finísima capa de lectura/escritura invisible solo para el Contenedor #4. Cuando intenta modificar un archivo del Ubuntu base, OverlayFS lo copia silenciosamente a la capa superior, y modifica la copia. El Ubuntu base permanece matemáticamente puro y compartido por todos.

---

## 4. 🧠 Internals Avanzados: El Fin de Docker y la era de `containerd`

Kubernetes, en 2020, anunció que **dejaba de soportar Docker**. 
¿Por qué? Porque Docker se había vuelto demasiado gordo. Contenía herramientas para desarrolladores (`docker build`, Docker Swarm, interfaz gráfica). Kubernetes (el orquestador de Google) solo necesita arrancar procesos de Linux puros de la forma más rápida y matemática posible.
Hoy en día, la industria corporativa SRE utiliza el **CRI (Container Runtime Interface)** estándar.
Bajo el capó de Kubernetes o Cloud Run, ya no corre el demonio de Docker, corren herramientas ultraligeras como **`containerd`** o **`CRI-O`**, que hablan directamente con el Kernel de Linux (Namespaces y Cgroups) ahorrando CPU y milisegundos valiosos, dejando a Docker solo como una herramienta amigable de escritorio para los programadores.

---
---

# 🛑 [DEEP-DIVE] C Kernel System Calls (`clone()` y `unshare()`)

Para comprender los fundamentos matemáticos y del Sistema Operativo de un contenedor, es necesario descender al código fuente en C del kernel de Linux (System Calls). "Contenedor" no existe como struct en el kernel; solo existen los procesos creados bajo un conjunto específico de Flags bit a bit en las llamadas del sistema.

## 5. El mecanismo real: La Syscall `clone()`

Cuando usamos `docker run` o `kubectl apply`, la herramienta de bajo nivel (`runc`) no invoca un proceso especial de "contenedorización". Invoca la system call POSIX estándar `clone()` que es la que se usa para crear *threads* (hilos) en C, pero pasándole un conjunto especial de flags de aislamiento (aislamiento por Namespaces).

```c
#define _GNU_SOURCE
#include <sched.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

// La función que ejecutará nuestro "contenedor" falso
int container_main(void *arg) {
    printf("Contenedor: [%5d] - ¡Hola, veo este PID como 1!\n", getpid());
    // Exec de la aplicación real (ej. bash, o tu microservicio Go)
    system("/bin/bash");
    return 1;
}

int main() {
    printf("Host SRE: [%5d] - Arrancando contenedor...\n", getpid());

    // Asignamos una pila (stack) de memoria separada para el nuevo proceso
    char *stack = malloc(1024 * 1024); // 1MB Stack
    if (stack == NULL) {
        perror("malloc");
        exit(1);
    }

    // EL SECRETO ARQUITECTÓNICO: La llamada clone() con Flags BIT-A-BIT
    // CLONE_NEWPID: Crea un nuevo Namespace de IDs de procesos (PIDs).
    // CLONE_NEWNET: Crea un nuevo Namespace de Redes (tarjetas eth aisladas).
    // CLONE_NEWNS:  Crea un nuevo Namespace de Mount (sistema de archivos).
    // SIGCHLD:      Envía señal al padre cuando el contenedor muere.
    int flags = CLONE_NEWPID | CLONE_NEWNET | CLONE_NEWNS | SIGCHLD;

    // Aquí nace el "contenedor" a los ojos del Kernel de Linux.
    pid_t container_pid = clone(container_main, stack + (1024 * 1024), flags, NULL);

    if (container_pid == -1) {
        perror("clone falló");
        exit(1);
    }

    printf("Host SRE: Contenedor arrancado con PID real de host: %d\n", container_pid);
    
    // Esperamos a que el contenedor termine
    waitpid(container_pid, NULL, 0);
    printf("Host SRE: Contenedor finalizado.\n");

    return 0;
}
```

### Anatomía de los Flags
*   `CLONE_NEWPID`: La abstracción matemática de identidades. El proceso hijo y sus descendientes son asignados a un nuevo árbol de mapeo (Radix Tree) en el kernel. Cuando el proceso llama a `getpid()`, el kernel consulta el Namespace actual y mapea el PID real (ej. 4590) al ID virtual (ej. 1).
*   `CLONE_NEWNET`: Desconecta al proceso del stack TCP/IP principal del host. El proceso nace "sordo y ciego" a internet. Solo puede comunicarse si el orquestador (Kubernetes / CNI) le inyecta físicamente un dispositivo `veth` (Virtual Ethernet) puenteando ambos namespaces.
*   `CLONE_NEWUTS`: Permite que el contenedor tenga un "Hostname" (nombre de máquina) propio.

## 6. Separación Dinámica: `unshare()` y `setns()`

Además de nacer aislado mediante `clone()`, un proceso existente puede aislarse dinámicamente o moverse entre namespaces.
*   **`unshare(CLONE_NEWNET)`**: Desacopla el proceso de su namespace de red actual y lo mete en uno nuevo. Útil para herramientas de seguridad SRE.
*   **`setns(fd, nstype)`**: Reacopla un proceso al namespace de otro proceso (cuyo file descriptor apunte a `/proc/<pid>/ns/net`). **Esto es exactamente lo que hace el comando `kubectl exec` o `docker exec`**. No "entra" mágicamente en la caja; simplemente invoca `setns` para acoplar el shell recién creado (`/bin/sh`) a los mismos Namespaces matemáticos del contenedor objetivo.

## 7. Cgroups v2 y Unified Hierarchy

Para completar la caja, `runc` inscribe el PID recién devuelto por `clone()` en el sistema de archivos virtual de Cgroups del Kernel (habitualmente `/sys/fs/cgroup`).
En la moderna arquitectura **cgroup v2**, el kernel usa una jerarquía matemática unificada (un solo árbol). Escribir un valor como `100000 100000` en el archivo `/sys/fs/cgroup/mi_contenedor/cpu.max` impone algorítmicamente un estrangulamiento estricto de cuotas en el *Completely Fair Scheduler (CFS)* de Linux, evitando que microservicios devoradores comprometan el host físico (Cloud Run).
