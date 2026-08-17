#!/usr/bin/env python3
"""
diagnostico_examen_interactivo.py
-------------------------------------------------------------------------
Examen Diagnóstico Interactivo por Temáticas (12 Facultades del Ecosistema)
-------------------------------------------------------------------------
Evalúa con 4 preguntas rigurosas por temática (48 preguntas en total) el
nivel de conocimiento del estudiante, califica de Nivel 0 a Nivel 3/4 en
cada eje, y genera un Plan de Estudio Personalizado adaptado a 5h/semana
en docs/formacion_ecosistema/MI_PLAN_DE_ESTUDIO_PERSONALIZADO.md sin alterar
ningún documento existente.
-------------------------------------------------------------------------
"""
import os
import sys
import json
import time
from pathlib import Path
from typing import Dict, List, Any

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
FORMACION_DIR = WORKSPACE_ROOT / "docs/formacion_ecosistema"
OUTPUT_PLAN_PATH = FORMACION_DIR / "MI_PLAN_DE_ESTUDIO_PERSONALIZADO.md"

QUESTION_BANK: Dict[str, Dict[str, Any]] = {
    "01_software_eng": {
        "title": "🏛️ Facultad I: Ingeniería de Software, DDD Puro & Testing",
        "module_path": "modulo_0_software_engineering",
        "questions": [
            {
                "q": "1. Según la Arquitectura Hexagonal y el Principio de Inversión de Dependencias (DIP), ¿qué dependencia está prohibida en la capa `domain/`?",
                "options": [
                    "A) Dependencias de tipos estándar del lenguaje como Records o Sealed Interfaces.",
                    "B) Anotaciones o librerías de infraestructura externa como Spring Framework, JPA/Hibernate o Jackson.",
                    "C) Constructores compactos de validación inmutable.",
                    "D) Interfaces que definen los puertos de salida (Outbound Ports)."
                ],
                "correct": "B",
                "feynman": "La receta de cocina (dominio) nunca debe depender de la marca del horno o de los platos (framework)."
            },
            {
                "q": "2. ¿Cuál es el beneficio de modelar entidades con Java 25 Records y Sealed Interfaces en lugar de clases tradicionales con setters?",
                "options": [
                    "A) Permite la herencia múltiple de clases concretas.",
                    "B) Garantiza inmutabilidad por diseño, constructores compactos O(1) y pattern matching exhaustivo en tiempo de compilación.",
                    "C) Genera automáticamente tablas en la base de datos SQL.",
                    "D) Evita tener que escribir pruebas unitarias."
                ],
                "correct": "B",
                "feynman": "Un record inmutable es como un contrato sellado con cera: nadie puede cambiar una cláusula por sorpresa después de firmado."
            },
            {
                "q": "3. En la política corporativa 'Zero-Mockito', ¿por qué se rechazan los mocks en pruebas unitarias del dominio?",
                "options": [
                    "A) Porque Mockito no compila en Java 25.",
                    "B) Porque los mocks acoplan los tests a la implementación interna en lugar del contrato, creando tests frágiles que pasan aunque el comportamiento sea incorrecto.",
                    "C) Porque los mocks consumen demasiada memoria RAM.",
                    "D) Porque Spring Boot 4 prohíbe el uso de interfaces."
                ],
                "correct": "B",
                "feynman": "Probar un coche simulando un motor falso de cartón (mock) te dice si gira el volante, pero no si el coche realmente arranca en la carretera."
            },
            {
                "q": "4. En el ciclo SDLC de 6 Fases (Toyota Kata / Doubt-Driven), ¿cuál es el único criterio válido para dar por verificado un componente?",
                "options": [
                    "A) Que el código compile sin advertencias en el IDE.",
                    "B) La aprobación verbal o asunción del desarrollador de que es thread-safe.",
                    "C) Un test ejecutable hermético en verde que demuestre empíricamente el comportamiento esperado.",
                    "D) Haber escrito un comentario explicativo en el encabezado de la clase."
                ],
                "correct": "C",
                "feynman": "La única prueba de que un puente resiste peso no es el dibujo del arquitecto, sino poner el camión encima y ver que no se cae."
            }
        ]
    },
    "02_sistemas_distribuidos": {
        "title": "🌐 Facultad II: Sistemas Distribuidos, Consenso & TLA+",
        "module_path": "modulo_0_sistemas_distribuidos",
        "questions": [
            {
                "q": "1. ¿Qué propiedad matemática fundamental define la relación de causalidad 'happened-before' de Leslie Lamport (1978)?",
                "options": [
                    "A) Dos eventos en nodos distintos siempre tienen la misma estampa de tiempo física UTC.",
                    "B) Si el evento A envía un mensaje y el evento B lo recibe, entonces A precede causalmente a B (A -> B), estableciendo un orden parcial estricto.",
                    "C) Todos los mensajes en una red viajan a la velocidad de la luz sin retardo.",
                    "D) El reloj del procesador central determina el orden global."
                ],
                "correct": "B",
                "feynman": "Es como enviar una carta por correo: es imposible que el destinatario lea la carta antes de que el remitente la haya escrito y enviado."
            },
            {
                "q": "2. ¿Cómo garantiza el algoritmo Raft (Ongaro 2014) que nunca coexistan dos líderes válidos en el mismo término (Split-Brain)?",
                "options": [
                    "A) Mediante un reloj GPS atómico sincronizado.",
                    "B) Exigiendo que para ser elegido líder se debe obtener el voto de una mayoría estricta (quórum > N/2) y cada nodo vota como máximo a un candidato por término.",
                    "C) Permitiendo que el nodo con la IP más baja siempre gane.",
                    "D) Reiniciando todos los servidores simultáneamente cada 5 segundos."
                ],
                "correct": "B",
                "feynman": "En un pueblo de 5 jueces, para elegir alcalde necesitas al menos 3 votos. Es matemáticamente imposible que dos candidatos consigan 3 votos a la vez."
            },
            {
                "q": "3. Según el Teorema PACELC (extensión del Teorema CAP de Abadi), si un sistema no sufre partición de red (Else), ¿cuál es el dilema de compensación fundamental?",
                "options": [
                    "A) Entre Consistencia (Consistency) y Disponibilidad (Availability).",
                    "B) Entre Latencia (Latency) y Consistencia (Consistency).",
                    "C) Entre CPU y Ancho de Banda.",
                    "D) Entre Seguridad y Escalabilidad."
                ],
                "correct": "B",
                "feynman": "Cuando no hay averías en la carretera: o esperas a que todos los almacenes confirmen el stock (más lento/consistente) o respondes al cliente al instante asumiendo pequeños desfases (más rápido/latencia)."
            },
            {
                "q": "4. En la especificación formal con TLA+/PlusCal, ¿cuál es la diferencia entre un invariante de Seguridad (Safety) y una propiedad de Vivacidad (Liveness)?",
                "options": [
                    "A) Safety verifica el rendimiento y Liveness verifica la memoria.",
                    "B) Safety garantiza que 'nada malo ocurra nunca' (ej. no deadlock), mientras que Liveness garantiza que 'algo bueno eventualmente ocurrirá' (ej. la transacción termina).",
                    "C) Safety solo aplica a bases de datos y Liveness a interfaces gráficas.",
                    "D) TLA+ no puede verificar propiedades de vivacidad."
                ],
                "correct": "B",
                "feynman": "Seguridad es asegurar que el semáforo nunca se ponga en verde para dos coches a la vez; Vivacidad es asegurar que el semáforo no se quede en rojo para siempre."
            }
        ]
    },
    "03_runtime_jvm_memoria": {
        "title": "☕ Facultad III: Runtime JVM, Loom, Leyden & Memoria",
        "module_path": "modulo_1_backend_java_spring",
        "questions": [
            {
                "q": "1. ¿Cómo resuelve Java 25 (JEP 491) el problema de 'Carrier Thread Pinning' en Virtual Threads cuando se usan bloques `synchronized`?",
                "options": [
                    "A) Reemplazando todos los bloques synchronized por variables volátiles.",
                    "B) Reimplementando los monitores de objetos en HotSpot para desanclar (unmount) la continuación del hilo portador durante bloqueos de I/O o contención.",
                    "C) Asignando un hilo del kernel de Linux dedicado a cada Virtual Thread.",
                    "D) Ejecutando el código en NodeJS mediante WebAssembly."
                ],
                "correct": "B",
                "feynman": "Si un camarero ve que el cocinero tarda, en vez de quedarse congelado delante de la cocina, deja la comanda apuntada en una mesa y atiende a otros comensales."
            },
            {
                "q": "2. ¿Cuál es el mecanismo principal de Project Leyden (AOT CDS) para lograr un arranque de Spring Boot en < 80 ms en Cloud Run?",
                "options": [
                    "A) Compilar todo el código a scripts Bash.",
                    "B) Grabar el grafo de clases cargadas, metadatos y heap calentado durante una fase de entrenamiento en un archivo `.jsa` precargado con mmap.",
                    "C) Desactivar la recolección de basura por completo.",
                    "D) Eliminar todas las dependencias del `pom.xml`."
                ],
                "correct": "B",
                "feynman": "En vez de montar los muebles de IKEA pieza a pieza cada vez que entras a la oficina, traes los muebles ya ensamblados en un camión."
            },
            {
                "q": "3. En el Java Memory Model (JMM) y la arquitectura de CPU moderna (MESI), ¿qué garantiza escribir en una variable declarada como `volatile`?",
                "options": [
                    "A) Que la variable se almacena en el disco duro.",
                    "B) Una relación 'happens-before', vaciando el store buffer del núcleo y evitando el reordenamiento de instrucciones en la caché L1/L2.",
                    "C) Que la variable solo puede ser leída por un único hilo.",
                    "D) Bloqueo exclusivo a nivel de sistema operativo."
                ],
                "correct": "B",
                "feynman": "Es como encender una luz roja en un panel visible para todos los operarios: nadie puede asumir que la tarea no ha cambiado de estado."
            },
            {
                "q": "4. ¿Cuál es la ventaja arquitectónica de Generational ZGC frente a G1 GC en aplicaciones con heaps grandes (> 16 GB)?",
                "options": [
                    "A) Que no requiere memoria RAM.",
                    "B) Pausas de GC concurrentes y sub-milisegúndicas (< 1 ms) independientes del tamaño del Heap mediante barreras de carga coloreadas (colored pointers).",
                    "C) Que compila a código nativo C++.",
                    "D) Que no genera objetos temporales."
                ],
                "correct": "B",
                "feynman": "Es como un equipo de limpieza silencioso que barre los pasillos mientras la fábrica sigue trabajando a pleno rendimiento, sin tener que parar la producción."
            }
        ]
    },
    "04_concurrencia_go_csp": {
        "title": "🐹 Facultad IV: Concurrencia Go, Runtime CSP & Memoria",
        "module_path": "modulo_2_go_y_concurrencia",
        "questions": [
            {
                "q": "1. En el planificador M:N de Go, ¿cómo funciona el mecanismo de 'Work Stealing'?",
                "options": [
                    "A) Si un hilo del SO se bloquea, mata a todos los demás hilos.",
                    "B) Si la cola local de un procesador lógico (P) se vacía, roba la mitad de las goroutines ejecutables de la cola local de otro P aleatorio.",
                    "C) Asigna memoria RAM de un proceso a otro sin permiso.",
                    "D) Delega todas las tareas al kernel de Linux."
                ],
                "correct": "B",
                "feynman": "Si una cajera del supermercado se queda sin clientes en su fila, atiende a la mitad de la fila de su compañera más saturada."
            },
            {
                "q": "2. ¿Qué ocurre si intentas enviar un valor a un canal sin buffer (`ch := make(chan int)`) y no hay ninguna goroutine esperando en lectura?",
                "options": [
                    "A) El valor se pierde silenciosamente.",
                    "B) La goroutine emisora se bloquea en la cola de espera (`sudog`) hasta que un receptor esté disponible.",
                    "C) El programa entra en pánico de inmediato.",
                    "D) El canal se convierte automáticamente en un buffer infinito."
                ],
                "correct": "B",
                "feynman": "Es una entrega en mano: quien entrega el paquete se queda esperando de pie hasta que la otra persona extiende la mano para recogerlo."
            },
            {
                "q": "3. En la optimización de memoria en Go, ¿cómo ayuda el 'Escape Analysis' (`go build -gcflags=\"-m\"`)?",
                "options": [
                    "A) Analiza si el código tiene vulnerabilidades de red.",
                    "B) Determina en tiempo de compilación si una variable puede asignarse en el Stack (coste cero de GC) o si debe escapar al Heap.",
                    "C) Evita el uso de punteros en todo el proyecto.",
                    "D) Cifra los ejecutables binarios."
                ],
                "correct": "B",
                "feynman": "Si usas un bolígrafo en tu propio escritorio, lo tiras a la papelera al salir (Stack); si se lo prestas a alguien de otra oficina, tienes que dejarlo registrado en el inventario central (Heap)."
            },
            {
                "q": "4. ¿Cuál es la estrategia recomendada en Go para procesar millones de peticiones sin sobrecargar el Garbage Collector?",
                "options": [
                    "A) Asignar nuevos slices con `make()` en cada petición.",
                    "B) Reutilizar estructuras y buffers de memoria mediante `sync.Pool` o ring-buffers preasignados.",
                    "C) Reiniciar el microservicio cada 1000 peticiones.",
                    "D) Desactivar el Garbage Collector con `GOGC=off` indefinidamente."
                ],
                "correct": "B",
                "feynman": "En un restaurante con miles de clientes, lavas y reutilizas los platos de vajilla (sync.Pool) en vez de comprar platos de plástico nuevos y tirarlos a la basura cada 5 segundos."
            }
        ]
    },
    "05_gemelo_digital_tensores": {
        "title": "🌐 Facultad V: Gemelo Digital, Redes Tensoriales PEPS & EnKF",
        "module_path": "modulo_3_unified_twin_math",
        "questions": [
            {
                "q": "1. ¿Por qué las Redes Tensoriales PEPS (Projected Entangled Pair States) son idóneas para simular redes acopladas (redes eléctricas, agua, tráfico)?",
                "options": [
                    "A) Porque solo admiten números enteros positivos.",
                    "B) Porque capturan correlaciones espaciales en 2D/3D respetando la 'Ley del Área', permitiendo contracciones eficientes en O(N) sin explosión combinatoria.",
                    "C) Porque no requieren operaciones de álgebra lineal.",
                    "D) Porque sustituyen a todas las bases de datos relacionales."
                ],
                "correct": "B",
                "feynman": "Para calcular cómo vibra una tela de araña enorme, no necesitas calcular todas las combinaciones posibles de hilos, solo cómo interactúa cada nudo con sus vecinos directos."
            },
            {
                "q": "2. En la asimilación de datos con el Filtro de Kalman por Conjuntos (EnKF), ¿cómo se evalúa el estado del sistema?",
                "options": [
                    "A) Mediante un único valor promedio estático.",
                    "B) Propagando un conjunto (ensamble) de estados mediante ecuaciones no lineales y actualizando la matriz de covarianza de error con cada observación real.",
                    "C) Descartando todas las mediciones ruidosas de los sensores.",
                    "D) Calculando la derivada analítica exacta de orden 10."
                ],
                "correct": "B",
                "feynman": "Para saber la trayectoria de un huracán, lanzas 100 simulaciones con ligeras variaciones climáticas; donde coinciden la mayoría, ahí está la mayor probabilidad."
            },
            {
                "q": "3. En cálculo estocástico, ¿qué diferencia al Lema de Itô del cálculo diferencial tradicional de Newton/Leibniz?",
                "options": [
                    "A) El Lema de Itô ignora las derivadas de primer orden.",
                    r"B) Incluye un término de corrección de segundo orden \((1/2) \sigma^2 f''(X_t) dt\) debido a la variación cuadrática no nula del Movimiento Browniano (\((dW_t)^2 = dt\)).",
                    "C) Solo funciona con variables deterministas.",
                    "D) Se aplica únicamente a números primos."
                ],
                "correct": "B",
                "feynman": "Si caminas por un suelo que tiembla caóticamente, tu avance no es solo la velocidad a la que andas, sino también el desvío acumulado por las sacudidas constantes del suelo."
            },
            {
                "q": "4. ¿Cuál es el rol de las PINNs (Physics-Informed Neural Networks) en el Gemelo Digital?",
                "options": [
                    "A) Generar imágenes artísticas de las turbinas.",
                    "B) Incrustar las leyes físicas fundamentales (ej. Navier-Stokes, Saint-Venant) directamente en la función de pérdida de la red neuronal como regularizador diferencial.",
                    "C) Eliminar la necesidad de entrenar la red con datos.",
                    "D) Sustituir el compilador de Python por C++."
                ],
                "correct": "B",
                "feynman": "Es como enseñarle a un piloto novato a volar con un instructor que le penaliza con toques de atención cada vez que intenta violar la ley de la gravedad."
            }
        ]
    },
    "06_edge_ai_litert": {
        "title": "🤖 Facultad VI: Edge AI LiteRT, Transformers & Verificación Formal",
        "module_path": "modulo_4_frontend_y_motores_ui",
        "questions": [
            {
                "q": "1. ¿Por qué la cuantización INT8 (Post-Training Quantization) es crítica en modelos Edge desplegados con LiteRT?",
                "options": [
                    "A) Porque aumenta la resolución visual de las imágenes.",
                    "B) Porque reduce el tamaño del modelo en ~75%, minimiza el ancho de banda de memoria y aprovecha las instrucciones SIMD enteras reduciendo el consumo de batería.",
                    "C) Porque duplica el número de parámetros del modelo.",
                    "D) Porque permite ejecutar código Python nativo en el navegador."
                ],
                "correct": "B",
                "feynman": "Pesar paquetes con una báscula que marca gramos exactos (INT8) es 10 veces más rápido y gasta menos batería que una báscula láser de precisión nanométrica (FP32)."
            },
            {
                "q": r"2. En la arquitectura Transformer, ¿cuál es la complejidad computacional del mecanismo de auto-atención estándar respecto a la longitud de la secuencia \(N\)?",
                "options": [
                    "A) O(1)",
                    "B) O(N)",
                    "C) O(N^2)",
                    "D) O(log N)"
                ],
                "correct": "C",
                "feynman": "Si hay 10 personas en una sala, cada persona debe mirar a los ojos a las otras 9 personas (10x10 = 100 miradas cruzadas)."
            },
            {
                "q": "3. ¿Cómo acelera el algoritmo HNSW (Hierarchical Navigable Small World) la búsqueda de similitud vectorial en bases de datos de embeddings?",
                "options": [
                    "A) Ordenando todos los vectores alfabéticamente.",
                    "B) Construyendo un grafo jerárquico de múltiples capas con saltos largos en las capas superiores y enlaces densos locales en la base (complejidad O(log N)).",
                    "C) Calculando la distancia euclídea exacta contra todos los vectores de la base de datos.",
                    "D) Comprimiendo los vectores con formato ZIP."
                ],
                "correct": "B",
                "feynman": "Para viajar de Madrid a un pueblo pequeño en Japón, primero tomas un vuelo internacional entre capitales (capa alta) y luego un tren local y un taxi (capa base)."
            },
            {
                "q": "4. En la arquitectura Neuro-Simbólica, ¿qué función cumple un Solver SMT (como Z3) al combinarse con un LLM?",
                "options": [
                    "A) Generar respuestas poéticas y creativas.",
                    "B) Validar formal y matemáticamente si las restricciones, planes o código generado por el LLM satisfacen invariantes estrictos sin alucinaciones.",
                    "C) Acelerar el renderizado de gráficos 3D.",
                    "D) Traducir texto entre idiomas."
                ],
                "correct": "B",
                "feynman": "El LLM es el arquitecto creativo que diseña el edificio sobre el plano; el Solver SMT es el ingeniero de cálculo de estructuras que demuestra matemáticamente si el edificio se cae o no."
            }
        ]
    },
    "07_cloud_native_gcp": {
        "title": "☁️ Facultad VII: Cloud-Native, BigQuery, Serverless & FinOps",
        "module_path": "modulo_5_cloud_native_dbs",
        "questions": [
            {
                "q": "1. ¿Por qué en BigQuery es obligatorio configurar `requirePartitionFilter = true` en tablas analíticas masivas?",
                "options": [
                    "A) Porque sin esa opción la base de datos se borra automáticamente.",
                    "B) Para forzar que toda consulta filtre por fecha/hora (`_PARTITIONDATE`), evitando escaneos completos de terabytes y protegiendo el presupuesto FinOps.",
                    "C) Porque BigQuery no soporta índices tradicionales B-Tree.",
                    "D) Para obligar a usar sentencias JOIN en todas las consultas."
                ],
                "correct": "B",
                "feynman": "Si vas a buscar un libro en una biblioteca gigante, obligas a mirar solo en la estantería del año 2026 en vez de revisar los 10 millones de libros del edificio."
            },
            {
                "q": "2. En Google Cloud Run (Serverless), ¿cuál es la ventaja de utilizar el aislamiento gVisor a nivel de contenedor?",
                "options": [
                    "A) Permite acceso directo e irrestricto al hardware de la GPU física.",
                    "B) Intercepta las llamadas al sistema (syscalls) en espacio de usuario, proporcionando un entorno hermético multi-inquilino de seguridad reforzada.",
                    "C) Hace que las imágenes Docker pesen menos de 1 MB.",
                    "D) Convierte automáticamente aplicaciones Java a Go."
                ],
                "correct": "B",
                "feynman": "Es como poner una cabina de cristal blindada alrededor de cada huésped de un hotel: puede pedir cosas por el interfono, pero no puede tocar las tuberías comunes."
            },
            {
                "q": "3. En una arquitectura Streaming ETL hacia BigQuery, ¿cuál es la ventaja de la Storage Write API frente a `tabledata.insertAll`?",
                "options": [
                    "A) Que solo admite archivos CSV de texto plano.",
                    "B) Soporta ingesta en streaming con micro-batches transaccionales gRPC en formato binario Protocol Buffers a menor coste y con semántica Exactly-Once.",
                    "C) Que no cobra por los datos almacenados.",
                    "D) Que no requiere conexión a internet."
                ],
                "correct": "B",
                "feynman": "En vez de enviar 1.000 sobres de papel individuales por correo postal ordinario, envías un contenedor de transporte sellado por tren de alta velocidad."
            },
            {
                "q": "4. Para cumplir el objetivo corporativo FinOps de coste < 0.015 USD/MAU/mes, ¿cuál es la regla de oro arquitectónica?",
                "options": [
                    "A) Mantener instancias de Compute Engine encendidas 24/7 con tamaños sobredimensionados.",
                    "B) Diseñar componentes serverless con escala a cero, ingesta micro-batch O(1) in-memory y dry-run preventivo en consultas analíticas.",
                    "C) Usar únicamente bases de datos en disco local sin copias de seguridad.",
                    "D) No monitorizar los costes de infraestructura."
                ],
                "correct": "B",
                "feynman": "Apagas las luces de las habitaciones vacías, compras los billetes de tren en grupo con descuento y miras el precio antes de pulsar el botón de comprar."
            }
        ]
    },
    "08_industrial_colas_hci": {
        "title": "🏭 Facultad VIII: Ingeniería Industrial, Teoría de Colas & Ergonomía",
        "module_path": "modulo_0_ingenieria_industrial",
        "questions": [
            {
                "q": r"1. Según la Ley de Little (\(L = \lambda W\)), si la tasa de llegada de peticiones (\(\lambda\)) se duplica y el tiempo medio de respuesta (\(W\)) se mantiene constante, ¿qué ocurre con el número medio de peticiones concurrentes en el sistema (\(L\))?",
                "options": [
                    "A) Se reduce a la mitad.",
                    "B) Se duplica exactamente.",
                    "C) Se eleva al cuadrado.",
                    "D) Permanece en cero."
                ],
                "correct": "B",
                "feynman": "Si entran el doble de personas por hora a una tienda y cada una pasa el mismo tiempo comprando, habrá exactamente el doble de personas dentro de la tienda."
            },
            {
                "q": r"2. En un sistema de colas M/M/1, ¿qué sucede con el tiempo de espera cuando la utilización del servidor (\(\rho = \lambda / \mu\)) se aproxima a 1.0 (100%)?",
                "options": [
                    "A) El tiempo de espera disminuye de forma lineal.",
                    r"B) El tiempo de espera tiende a infinito de forma asintótica no lineal (\(W = 1 / (\mu - \lambda)\)).",
                    "C) El sistema procesa las peticiones instantáneamente.",
                    "D) La tasa de servicio se multiplica por diez."
                ],
                "correct": "B",
                "feynman": "Si una autopista está al 99% de su capacidad, cualquier frenazo minúsculo provoca un atasco monumental kilométrico interminable."
            },
            {
                "q": "3. En Lean Manufacturing y desarrollo ágil, ¿cuál de las siguientes opciones representa una 'Muda' (desperdicio) de Inventario en Software?",
                "options": [
                    "A) Código en ramas no integradas durante meses o historias de usuario a medio hacer que no aportan valor en producción.",
                    "B) Escribir pruebas unitarias automatizadas con Testcontainers.",
                    "C) Documentar la arquitectura con diagramas Mermaid.",
                    "D) Automatizar los despliegues con GitOps."
                ],
                "correct": "A",
                "feynman": "Tener cajas de piezas acumuladas en un almacén cogiendo polvo sin montar coches es exactamente igual que tener cientos de líneas de código en ramas olvidadas."
            },
            {
                "q": "4. En la optimización de Core Web Vitals, ¿qué métrica mide la capacidad de respuesta a la interacción del usuario y cuál es su umbral de excelencia?",
                "options": [
                    "A) LCP (Largest Contentful Paint) < 5.0 s",
                    "B) CLS (Cumulative Layout Shift) > 0.5",
                    "C) INP (Interaction to Next Paint) < 200 ms",
                    "D) TTFB (Time to First Byte) < 10 ms"
                ],
                "correct": "C",
                "feynman": "Cuando tocas el botón de la luz en la pared, el interruptor debe encender la bombilla en menos de un parpadeo (200 ms) para que no pienses que está roto."
            }
        ]
    },
    "09_geoespacial_h3_osrm": {
        "title": "🗺️ Facultad IX: Ingeniería Geoespacial H3, Ruteo OSRM & Movilidad",
        "module_path": "modulo_8_ingenieria_geoespacial_h3_osrm",
        "questions": [
            {
                "q": "1. ¿Por qué Uber diseñó el sistema H3 basado en hexágonos regulares en lugar de una cuadrícula de cuadrados?",
                "options": [
                    "A) Porque los hexágonos son más fáciles de dibujar en CSS.",
                    "B) Porque todos los hexágonos vecinos contiguos tienen exactamente la misma distancia de centro a centro, evitando distorsiones de vecindad de las esquinas.",
                    "C) Porque los hexágonos ocupan menos memoria que los números enteros.",
                    "D) Porque los satélites GPS solo transmiten coordenadas hexagonales."
                ],
                "correct": "B",
                "feynman": "En un tablero de ajedrez cuadrado, las casillas en diagonal están más lejos que las de los lados. En un panal de abejas, todos los vecinos están a la misma distancia exacta."
            },
            {
                "q": "2. ¿Cómo logran las Jerarquías de Contracción (Contraction Hierarchies en OSRM) calcular la ruta más corta entre dos puntos en < 2 ms en grafos continentales?",
                "options": [
                    "A) Usando el algoritmo de fuerza bruta sobre todos los caminos posibles.",
                    "B) Precalculando atajos (*shortcuts*) jerárquicos entre nodos importantes durante una fase offline y realizando una búsqueda bidireccional de Dijkstra hacia arriba (*upward search*).",
                    "C) Almacenando todas las combinaciones de rutas en una tabla SQL.",
                    "D) Conectándose a Google Maps mediante API externa."
                ],
                "correct": "B",
                "feynman": "Para ir de tu casa en Toledo a una calle de París, no buscas cruce por cruce: vas a la autopista principal nacional, viajas por ella y bajas a la calle local solo al llegar."
            },
            {
                "q": "3. En los algoritmos de despacho de flotas de movilidad, ¿cómo resuelve el algoritmo de Kuhn-Munkres (Húngaro) la asignación óptima?",
                "options": [
                    "A) Asignando al azar el primer vehículo que responde.",
                    "B) Encontrando el emparejamiento perfecto de peso mínimo en un grafo bipartito (conductores y pasajeros) en complejidad polinómica O(V^3).",
                    "C) Cancelando todos los viajes si no hay vehículos suficientes.",
                    "D) Ordenando los conductores por orden de llegada."
                ],
                "correct": "B",
                "feynman": "Es como repartir los regalos de navidad entre los niños para que el descontento total de todos juntos sea el mínimo posible."
            },
            {
                "q": "4. En los modelos de tarificación dinámica (*Surge Pricing*), ¿por qué se utiliza una función sigmoide acotada frente a una lineal pura?",
                "options": [
                    "A) Porque la función lineal no se puede calcular en computadoras.",
                    "B) Para evitar picos infinitos de precio ante escasez extrema y mantener un multiplicador suave con asíntota superior definida.",
                    "C) Para que el precio nunca supere el 10% del viaje.",
                    "D) Porque los conductores exigen precios fijos."
                ],
                "correct": "B",
                "feynman": "Si llueve mucho y hay pocos taxis, el precio sube gradualmente para atraer a más conductores, pero tiene un techo máximo para no cobrar 1.000 euros por un viaje de 5 minutos."
            }
        ]
    },
    "10_fintech_stripe_sagas": {
        "title": "💳 Facultad X: Fintech, Stripe Connect, Sagas & Escrow",
        "module_path": "modulo_9_fintech_facturacion_stripe_sagas",
        "questions": [
            {
                "q": "1. En una plataforma multi-tenant con fondos en custodia (Escrow) mediante Stripe Connect, ¿cuál es el flujo de fondos correcto?",
                "options": [
                    "A) Cobrar directamente en la cuenta bancaria personal del conductor sin pasar por la plataforma.",
                    "B) Cobrar el cargo en la cuenta de la plataforma mediante un Destination Charge y retener los fondos hasta que el servicio se verifique satisfactoriamente antes de liberar la transferencia.",
                    "C) Almacenar los datos de la tarjeta de crédito en texto plano en la base de datos local.",
                    "D) Enviar el dinero en efectivo mediante mensajero."
                ],
                "correct": "B",
                "feynman": "Es como un árbitro en una compraventa: el comprador le entrega el dinero al árbitro; cuando el comprador recibe el producto perfecto, el árbitro le entrega el dinero al vendedor."
            },
            {
                "q": "2. ¿Por qué el Patrón Sagas sustituye al protocolo Two-Phase Commit (2PC) en arquitecturas de microservicios distribuidos?",
                "options": [
                    "A) Porque Sagas no permite transacciones monetarias.",
                    "B) Porque 2PC requiere bloqueos síncronos distribuidos que reducen la disponibilidad y provocan cuellos de botella masivos ante caídas de red, mientras que Sagas utiliza transacciones locales con acciones compensatorias.",
                    "C) Porque 2PC solo funciona en servidores Windows.",
                    "D) Porque Sagas elimina la necesidad de bases de datos."
                ],
                "correct": "B",
                "feynman": "En vez de congelar a todos los empleados del banco esperando una llamada telefónica conjunta (2PC), cada sucursal anota su parte y, si algo falla a mitad de camino, ejecutan una operación de reembolso (compensación)."
            },
            {
                "q": "3. ¿Cuál es el propósito fundamental de enviar un `Idempotency-Key` en las peticiones de cobro a una pasarela de pagos?",
                "options": [
                    "A) Cifrar la contraseña del usuario.",
                    "B) Garantizar que si la conexión de red se interrumpe y la petición se reintenta, la pasarela de pagos procese el cobro exactamente una sola vez sin duplicar el cargo.",
                    "C) Aumentar la velocidad de la tarjeta de crédito.",
                    "D) Aplicar un descuento comercial al cliente."
                ],
                "correct": "B",
                "feynman": "Si le das al botón de comprar y la pantalla se queda congelada, puedes volver a pulsar sin miedo: la pasarela sabe que es el mismo ticket y no te cobra dos veces."
            },
            {
                "q": "4. En contabilidad financiera y sistemas de libros mayores (*Ledger*), ¿cuál es la regla sagrada de la partida doble (*Double-Entry Bookkeeping*)?",
                "options": [
                    "A) Que cada transacción debe anotarse únicamente en una sola cuenta.",
                    r"B) Que la suma total de los Débitos debe ser exactamente igual a la suma total de los Créditos (\(\sum \text{Débitos} = \sum \text{Créditos}\)), sin crear ni destruir dinero de la nada.",
                    "C) Que las cuentas bancarias pueden tener saldos negativos infinitos.",
                    "D) Que solo se registran las ganancias."
                ],
                "correct": "B",
                "feynman": "Si sacas 50 euros de tu bolsillo (crédito), esos mismos 50 euros deben aparecer exactamente en la caja del supermercado (débito). La suma total de entradas y salidas da siempre cero."
            }
        ]
    },
    "11_identidad_zero_trust": {
        "title": "🔐 Facultad XI: Identidad, Criptografía & Zero-Trust BeyondCorp",
        "module_path": "modulo_10_identidad_zero_trust_beyondcorp",
        "questions": [
            {
                "q": "1. ¿Cuál es el postulado fundamental de la Arquitectura Zero-Trust (BeyondCorp / NIST SP 800-207)?",
                "options": [
                    "A) 'Confía en todos los dispositivos que estén dentro de la red corporativa de la oficina'.",
                    "B) 'Nunca confíes, verifica siempre': ningún usuario, dispositivo o servicio tiene acceso implícito basado únicamente en su ubicación de red física o IP.",
                    "C) 'Desactiva todos los cortafuegos y contraseñas'.",
                    "D) 'Utiliza una única VPN central para todos los accesos'."
                ],
                "correct": "B",
                "feynman": "No basta con que alguien haya cruzado la puerta del edificio: cada puerta, armario y cajón de la oficina tiene un guardia que le pide el carnet y la huella digital cada vez que intenta abrirlo."
            },
            {
                "q": "2. ¿Cómo funciona la verificación de tokens JWT mediante el mecanismo de rotación de claves JWKS (JSON Web Key Set)?",
                "options": [
                    "A) Enviando el token por correo electrónico a un administrador.",
                    "B) El emisor firma el JWT con su clave privada asimétrica; el receptor descarga periódicamente el conjunto de claves públicas (JWKS) del endpoint OIDC y valida la firma matemáticamente en memoria en O(1) sin consultar a la base de datos en cada petición.",
                    "C) Guardando la contraseña en texto plano en la cabecera HTTP.",
                    "D) Descifrando el token con una clave compartida hardcodeada en el código."
                ],
                "correct": "B",
                "feynman": "El rey sella los decretos con su anillo personal (clave privada). Los guardias de cada aduana tienen una copia del dibujo oficial del sello (clave pública JWKS) para verificar la autenticidad al instante sin tener que llamar al palacio en cada viaje."
            },
            {
                "q": "3. En el patrón Backend-for-Frontend (BFF), ¿por qué los tokens de acceso JWT y Refresh Tokens nunca deben almacenarse en el `localStorage` del navegador?",
                "options": [
                    "A) Porque ocupan demasiado espacio en disco.",
                    "B) Porque cualquier script malicioso inyectado mediante Cross-Site Scripting (XSS) puede leer el `localStorage` y robar la sesión completa; deben residir en cookies `HttpOnly`, `Secure` y `SameSite=Strict` gestionadas por el BFF.",
                    "C) Porque `localStorage` solo admite números enteros.",
                    "D) Porque los navegadores modernos borran el `localStorage` cada minuto."
                ],
                "correct": "B",
                "feynman": "Dejar la llave de tu casa en una mesa pública a la vista de cualquiera (localStorage) hace que un ladrón que mire por la ventana se la lleve; guardarla en una caja fuerte dentro de la pared con clave secreta (HttpOnly) protege la casa."
            },
            {
                "q": "4. En bases de datos NoSQL como Firestore, ¿cómo se implementa el Aislamiento Celular Multi-Tenant mediante Reglas de Seguridad (RLS)?",
                "options": [
                    "A) Creando una base de datos física distinta para cada cliente en servidores diferentes.",
                    "B) Extrayendo el `tenant_id` de los Custom Claims validados en el token de autenticación del usuario y comparándolo en las reglas del Edge (`request.auth.token.tenant_id == resource.data.tenant_id`).",
                    "C) Permitir que cualquier usuario autenticado lea todas las colecciones.",
                    "D) Desactivando las reglas de seguridad en producción."
                ],
                "correct": "B",
                "feynman": "Es como un casillero postal en correos: la llave de tu llave maestra solo puede abrir los buzones que tengan tu mismo número de abonado grabado en la puerta."
            }
        ]
    },
    "12_supply_chain_slsa": {
        "title": "📦 Facultad XII: Supply Chain Security SLSA L3 & GitOps",
        "module_path": "modulo_11_supply_chain_security_slsa_gitops",
        "questions": [
            {
                "q": "1. ¿Qué garantiza alcanzar el nivel de madurez SLSA Nivel 3 (Supply-chain Levels for Software Artifacts)?",
                "options": [
                    "A) Que el software no tiene ningún fallo lógico.",
                    "B) Que el artefacto fue generado en un entorno de compilación hermético, aislado y reproducible, con proveniencia autenticada y no falsificable generada automáticamente por el pipeline de CI/CD.",
                    "C) Que el código fue escrito por un equipo de más de 10 personas.",
                    "D) Que el contenedor Docker se ejecuta en modo root."
                ],
                "correct": "B",
                "feynman": "Es como comprar un medicamento con sello de laboratorio inalterable: tienes un certificado químico inviolable que demuestra exactamente qué ingredientes se usaron y en qué fábrica cerrada se envasó."
            },
            {
                "q": "2. ¿Cómo funciona la firma de contenedores OCI con Cosign y Sigstore sin necesidad de gestionar claves privadas tradicionales (Keyless Signing)?",
                "options": [
                    "A) No firma los contenedores realmente.",
                    "B) Utiliza OpenID Connect (OIDC) para autenticar la identidad del pipeline, emite un certificado X.509 de corta duración (Fulcio) y registra la firma en un log público de transparencia inmutable e inmutable (Rekor).",
                    "C) Almacena una contraseña en un archivo `.txt` dentro del contenedor.",
                    "D) Pide la firma manual de un usuario cada vez que se despliega."
                ],
                "correct": "B",
                "feynman": "Es como un notario digital instantáneo: comprueba el DNI del robot de compilación en 1 segundo, sella el paquete y anota la matrícula del paquete en un libro de registro público imborrable."
            },
            {
                "q": "3. En la metodología GitOps con herramientas como ArgoCD, ¿cuál es la Única Fuente de Verdad del estado del clúster?",
                "options": [
                    "A) La memoria RAM del servidor Kubernetes.",
                    "B) El repositorio Git que contiene los manifiestos declarativos; cualquier desviación manual en el clúster se detecta y corrige automáticamente (*Self-Healing*).",
                    "C) El panel de control del proveedor cloud.",
                    "D) El ordenador portátil del desarrollador."
                ],
                "correct": "B",
                "feynman": "El plano del arquitecto guardado en la biblioteca (Git) manda sobre la obra: si alguien mueve un ladrillo en la obra sin permiso, el robot de control vuelve a poner el ladrillo exactamente como decía el plano original."
            },
            {
                "q": "4. ¿Cuál es el propósito de generar un SBOM (Software Bill of Materials) en estándar CycloneDX o SPDX durante la compilación?",
                "options": [
                    "A) Reducir el tamaño de las imágenes Docker.",
                    "B) Proporcionar un inventario formal, estructurado y auditable de todas las librerías, dependencias directas y transitivas incluidas en el software para detectar vulnerabilidades (CVEs) al instante.",
                    "C) Acelerar la conexión Wi-Fi de los servidores.",
                    "D) Reemplazar el código fuente original de la aplicación."
                ],
                "correct": "B",
                "feynman": "Es la etiqueta de información nutricional e ingredientes de un alimento: si sanidad avisa de que un aditivo concreto es tóxico, puedes mirar la etiqueta y saber de inmediato si tu producto lo contiene."
            }
        ]
    }
}

def evaluate_level(score: int, total: int = 4) -> tuple[str, int]:
    """Retorna (nombre_nivel, nivel_numerico_0_a_3)"""
    if score == 0 or score == 1:
        return "Nivel 0: Iniciación (Requiere estudio completo de fundamentos)", 0
    elif score == 2:
        return "Nivel 1: Junior (Requiere consolidación práctica y primeros principios)", 1
    elif score == 3:
        return "Nivel 2: Senior (Requiere dominar internals y optimizaciones avanzadas)", 2
    else: # score == 4
        return "Nivel 3/4: Staff / Fellow (Dominio demostrado, convalidable o repaso rápido)", 3

def generate_personalized_plan(results: Dict[str, Any], hours_per_week: int = 5):
    """Genera el documento MI_PLAN_DE_ESTUDIO_PERSONALIZADO.md"""
    
    # Calcular necesidades horarias según nivel diagnosticado
    total_hours = 0
    thematic_plan = []
    
    faculty_hour_matrix = {
        0: 20, # Horas necesarias si nivel 0
        1: 12, # Horas necesarias si nivel 1
        2: 6,  # Horas necesarias si nivel 2
        3: 2   # Horas necesarias si nivel 3 (convalidado / repaso)
    }
    
    for key, data in results.items():
        score = data["score"]
        level_name, level_num = evaluate_level(score)
        hours_needed = faculty_hour_matrix[level_num]
        total_hours += hours_needed
        thematic_plan.append({
            "key": key,
            "title": data["title"],
            "score": score,
            "total": data["total"],
            "level_name": level_name,
            "level_num": level_num,
            "hours": hours_needed,
            "module_path": data["module_path"]
        })
        
    weeks = int((total_hours + hours_per_week - 1) // hours_per_week)
    
    # Generar contenido Markdown
    timestamp = time.strftime("%Y-%m-%d %H:%M:%S")
    
    lines = [
        "# 🗺️ MI PLAN DE ESTUDIO PERSONALIZADO & ITINERARIO ADAPTATIVO",
        f"## *Generado el {timestamp} tras Examen Diagnóstico Riguroso*",
        "",
        "> [!IMPORTANT]",
        f"> **Parámetros del Plan:** Dedicación de **{hours_per_week} horas a la semana** | Carga total calculada: **{total_hours} horas** (~**{weeks} semanas / {weeks/4:.1f} meses**).",
        "> **Política de Integridad:** Este plan utiliza la totalidad de la base teórica existente sin modificar ni eliminar ningún módulo de [`docs/formacion_ecosistema/`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/).",
        "",
        "---",
        "",
        "## 📊 1. Matriz de Resultados del Diagnóstico por Facultad",
        "",
        "| Facultad / Eje Temático | Aciertos | Diagnóstico de Nivel | Horas Asignadas | Módulos y Referencias Clave |",
        "| :--- | :---: | :--- | :---: | :--- |"
    ]
    
    for item in thematic_plan:
        path_link = f"[`{item['module_path']}`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/{item['module_path']}/)"
        lines.append(f"| **{item['title']}** | `{item['score']} / {item['total']}` | {item['level_name']} | **{item['hours']} h** | {path_link} |")
        
    lines.extend([
        "",
        "---",
        "",
        "## 🗓️ 2. Calendario de Ejecución Semana a Semana (5 Horas / Semana)",
        ""
    ])
    
    # Organizar el cronograma semana a semana
    current_week = 1
    current_week_hours = 0
    current_week_tasks = []
    
    # Ordenar por prioridad: Nivel 0 primero, luego Nivel 1, Nivel 2 y finalmente Nivel 3
    sorted_plan = sorted(thematic_plan, key=lambda x: x["level_num"])
    
    for item in sorted_plan:
        h_remaining = item["hours"]
        while h_remaining > 0:
            space = hours_per_week - current_week_hours
            allocated = min(h_remaining, space)
            current_week_tasks.append(f"- **{item['title']}** ({allocated} h): Estudio bajo Método Feynman en [`{item['module_path']}`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/{item['module_path']}/).")
            current_week_hours += allocated
            h_remaining -= allocated
            
            if current_week_hours >= hours_per_week:
                lines.append(f"### 📅 Semana {current_week} (5 horas)")
                lines.extend(current_week_tasks)
                lines.append("")
                current_week += 1
                current_week_hours = 0
                current_week_tasks = []
                
    if current_week_tasks:
        lines.append(f"### 📅 Semana {current_week} ({current_week_hours} horas)")
        lines.extend(current_week_tasks)
        lines.append("")
        
    lines.extend([
        "---",
        "",
        "## 🔄 3. Protocolo de Estudio Semanal (Feynman 5-Steps)",
        "Para cada bloque temático asignado:",
        "1. **Paso 1: Ancla Mental (30 min)**: Leer la analogía isomórfica y entender el modelo intuitivo.",
        "2. **Paso 2: Primeros Principios (1 h)**: Desglosar el comportamiento mecánico en CPU/RAM/Red.",
        "3. **Paso 3: Laboratorio Práctico (1.5 h)**: Ejecutar el código en [`laboratorios_practicos/`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/laboratorios_practicos/) o implementar la kata.",
        "4. **Paso 4: Paper Académico (1 h)**: Destilar el paper canónico en [`biblioteca_papers_pdf_rfc/`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/biblioteca_papers_pdf_rfc/).",
        "5. **Paso 5: Test de los 12 Años & Consilium (1 h)**: Explicar el concepto en papel sin tecnicismos y validar con `python3 scripts/feynman_interactive_tutor.py --quiz`.",
        "",
        "---",
        "*Plan personalizado generado bajo el Estándar Feynman. Certificado para su ejecución inmediata.*"
    ])
    
    OUTPUT_PLAN_PATH.write_text("\n".join(lines), encoding="utf-8")
    print(f"\n✅ ¡Plan de estudio personalizado generado con éxito!")
    print(f"👉 Archivo guardado en: {OUTPUT_PLAN_PATH}")

def run_interactive_cli():
    print("====================================================================")
    print("  🎓 EXAMEN DIAGNÓSTICO INTERACTIVO — 12 GRANDES FACULTADES")
    print("====================================================================")
    print("  Responde a las 4 preguntas de cada temática (A, B, C o D).")
    print("  Al finalizar, se generará tu plan de estudio personalizado adaptado a ti.")
    print("====================================================================\n")
    
    results = {}
    
    for fac_key, fac_data in QUESTION_BANK.items():
        print(f"\n====================================================================")
        print(f"  {fac_data['title']}")
        print(f"====================================================================")
        
        correct_in_faculty = 0
        total_in_faculty = len(fac_data["questions"])
        
        for q_idx, q in enumerate(fac_data["questions"], 1):
            print(f"\n{q['q']}")
            for opt in q["options"]:
                print(f"  {opt}")
                
            user_ans = ""
            while user_ans not in ["A", "B", "C", "D", "SKIP"]:
                try:
                    user_ans = input("\n👉 Tu respuesta (A/B/C/D) o 'SKIP' para omitir: ").strip().upper()
                except (EOFError, KeyboardInterrupt):
                    print("\nExamen interrumpido.")
                    return
                    
            if user_ans == q["correct"]:
                print("  ✅ ¡CORRECTO!")
                print(f"  🧠 Ancla Feynman: {q['feynman']}")
                correct_in_faculty += 1
            elif user_ans == "SKIP":
                print(f"  ⏭️ Omitida. Respuesta correcta: {q['correct']}")
                print(f"  🧠 Ancla Feynman: {q['feynman']}")
            else:
                print(f"  ❌ INCORRECTO. Respuesta correcta: {q['correct']}")
                print(f"  🧠 Ancla Feynman: {q['feynman']}")
                
        results[fac_key] = {
            "title": fac_data["title"],
            "module_path": fac_data["module_path"],
            "score": correct_in_faculty,
            "total": total_in_faculty
        }
        
    generate_personalized_plan(results)

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "--sample-run":
        # Generar con muestra predeterminada para verificar
        sample_results = {}
        for k, v in QUESTION_BANK.items():
            sample_results[k] = {
                "title": v["title"],
                "module_path": v["module_path"],
                "score": 2,
                "total": 4
            }
        generate_personalized_plan(sample_results)
    else:
        run_interactive_cli()
