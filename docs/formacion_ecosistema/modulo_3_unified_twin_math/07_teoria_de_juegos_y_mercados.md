# Módulo 3.7: Teoría de Juegos, Equilibrio de Nash y Diseño de Mecanismos

---

## 1. 🐣 Rincón Junior: El Dilema del Tráfico

Imagina que hay dos caminos para ir al trabajo: una Autopista ancha y un Atajo por el centro.
Si todos van por la Autopista, hay atasco y se tarda 50 minutos. Si todos van por el Atajo, las calles se bloquean y se tarda 60 minutos.
¿Qué pasa en la vida real? La gente prueba rutas hasta que **nadie** tiene un incentivo para cambiar de ruta por sí solo. Quizás el 70% va por la autopista y el 30% por el centro, y ambos grupos tardan exactamente 45 minutos. Si alguien cambia, tardará más.
Ese estado donde nadie quiere cambiar su decisión (porque empeoraría) se llama **Equilibrio de Nash**. La Teoría de Juegos es la ciencia de predecir el comportamiento matemático de entidades (humanos o IAs) egoístas y racionales interactuando.

---

## 2. 🔬 Fundamentos Teóricos: Juegos No Cooperativos y Equilibrio de Nash

En un juego matemático, tenemos Jugadores, Estrategias (Rutas, Precios) y Pagos (Tiempo, Dinero).
John Nash demostró matemáticamente en 1950 que, en cualquier juego finito donde se permite el azar (Estrategias Mixtas), **siempre existe al menos un Equilibrio**.

### La Paradoja de Braess (El horror del tráfico)
En el Gemelo Digital, usamos la Teoría de Juegos para simular el tráfico (Routing Games). Una de las revelaciones matemáticas más contraintuitivas es la Paradoja de Braess:
*"Construir una nueva carretera súper rápida en una red congestionada puede empeorar el tiempo de viaje para **todos** los conductores"*.
**¿Por qué?** Porque los conductores son egoístas (buscan el Equilibrio de Nash), no cooperativos (buscan el Óptimo Social). La nueva carretera atractiva atrae a tanta gente de rutas periféricas que colapsa el nodo central, destruyendo el equilibrio global.
Para resolver el tráfico en la app, no añadimos calles; añadimos **Peajes (Tolls)** calculados dinámicamente mediante el coste marginal de congestión (Pigouvian Taxes) para alinear el Nash Egoísta con el Óptimo Social.

---

## 3. 🚀 Arquitectura Práctica: Diseño de Mecanismos y Subastas

La Teoría de Juegos normal predice lo que pasa dadas unas reglas. El **Diseño de Mecanismos (Mechanism Design)** es la ciencia inversa: *"Quiero que el resultado sea X. ¿Qué reglas matemáticas (Subasta) debo programar para que los humanos/bots egoístas hagan X por su propio beneficio?"*.

### Subastas Vickrey (Segundo Precio)
Imagina que Google Ads o el algoritmo de despacho de Viajes subasta un viaje a varios conductores.
Si usamos una subasta normal (First-Price: el que puja más alto, gana y paga eso), los conductores mentirán. Si un viaje les cuesta 10€, pujarán 11€ para intentar ganar un euro, o 15€ si creen que el otro conductor pujará 14€. Es un juego mental ineficiente.

**Subasta de Segundo Precio (Vickrey)**:
El ganador es el que puja más alto, **pero solo paga lo que pujó el segundo más alto**.
*Magia Matemática*: Está demostrado por el Premio Nobel William Vickrey que, bajo estas reglas, **la estrategia dominante (la mejor matemáticamente siempre) es decir exactamente la verdad**.
Si el viaje te cuesta 10€, pujas 10€. No tienes que intentar adivinar qué pujarán los demás. Si ganas porque el segundo pujó 8€, ¡el sistema te cobra 8€ y ganas 2€ limpios! Si pujas más de la verdad (15€), te arriesgas a ganar y que el segundo pujara 12€, obligándote a pagar 12€ por algo que te cuesta 10€ (pierdes dinero).
Este algoritmo es el motor trillonario subyacente de casi toda la publicidad en internet (Ad-Tech) y asignación de recursos.

---

## 4. 🧠 Internals Avanzados: Matching Bipartito (Gale-Shapley)

Cuando no se trata de dinero, sino de emparejar dos grupos con preferencias (ej. Conductores con Pasajeros, o Estudiantes con Universidades).
El **Algoritmo de Gale-Shapley (Deferred Acceptance)** resuelve el problema del Matrimonio Estable en tiempo polinómico $O(N^2)$.
Garantiza matemáticamente que el emparejamiento final es **Estable**: No existirá nunca un Conductor A y un Pasajero B que se prefieran mutuamente más de lo que prefieren a sus parejas asignadas por el algoritmo.
Si no se usara este algoritmo en el Gemelo Digital, los conductores cancelarían viajes constantemente en la app porque encontrarían viajes "mejores" a sus espaldas.

---

## 5. ⚠️ Runbook SRE Matemático: Oscilaciones de Precios Dinámicos (Surge)

**Incidente**: El sistema de Surge Pricing (Precios Dinámicos) entra en oscilación violenta. En el minuto 1, el multiplicador es 3.0x. Todos los coches van a esa zona. En el minuto 2, hay exceso de coches, el multiplicador cae a 1.0x. Los coches huyen. En el minuto 3, vuelve a 3.0x. Los pasajeros no pueden pedir viajes de forma fiable.

**Diagnóstico Matemático (El problema del Rebaño / Herd Behavior)**:
El algoritmo de Surge local está creando un juego repetido donde la respuesta de los agentes (conductores) tiene un retardo (Delay) por el tiempo de conducción. Sistemas dinámicos con retardos y ganancias altas (Control Theory) son inestables por naturaleza (Polos en el semiplano derecho).

**Solución Inmediata SRE / Algorítmica**:
1.  **Amortiguación Espacial (Spatial Smoothing)**: No aplicar un Surge aislado a un hexágono H3. Aplicar un filtro Gaussiano (Convolución 2D) sobre los hexágonos vecinos para crear un gradiente de precios suave, evitando fronteras duras.
2.  **Amortiguación Temporal (Low-Pass Filter)**: Usar un filtro de media móvil exponencial (EMA) sobre el multiplicador de Surge, forzando a que la derivada de los precios $\frac{dP}{dt}$ nunca supere un umbral máximo.
3.  **Mecanismo de Lock-in**: Si un conductor acepta dirigirse a una zona Surge, congelar matemáticamente el precio prometido para ese conductor específico (Contrato de Futuros) independientemente de si la zona se satura cuando él llegue, eliminando el riesgo y el comportamiento oscilatorio.

---
---

# 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

El Teorema de Nash es un hito de las matemáticas del siglo XX. Afirma que si permitimos la aleatorización (Estrategias Mixtas), todo juego finito tiene al menos un equilibrio de Nash. La demostración utiliza topología (el Teorema del Punto Fijo de Kakutani o Brouwer).

## 6. Equilibrios de Nash en Estrategias Mixtas (Mixed Strategies)

A menudo en los juegos, no existe un equilibrio en estrategias puras (decisiones deterministas 100%).
Ejemplo clásico: El Juego de las Monedas (Matching Pennies) o el Penalti en el fútbol. Si el lanzador siempre tira a la derecha (Estrategia Pura), el portero siempre saltará a la derecha. El lanzador entonces cambiará a la izquierda. No hay estabilidad.

La solución es la **Estrategia Mixta**: El jugador asigna una Distribución de Probabilidad sobre sus acciones posibles. El equilibrio se alcanza cuando la distribución de probabilidad de un jugador hace que el oponente se vuelva **absolutamente indiferente** entre cualquiera de sus propias opciones.

### Demostración Analítica del Equilibrio Mixto (El Algoritmo de Indiferencia)

Supongamos un juego 2x2.
*   **Jugador 1** elige la acción $A$ con probabilidad $p$ y $B$ con probabilidad $(1-p)$.
*   **Jugador 2** elige la acción $C$ con probabilidad $q$ y $D$ con probabilidad $(1-q)$.

Matriz de Pagos para el Jugador 2 (filas = Jugador 1, columnas = Jugador 2):
*   Si J1 juega $A$: J2 gana $u(A,C)$ o $u(A,D)$
*   Si J1 juega $B$: J2 gana $u(B,C)$ o $u(B,D)$

Para que el Jugador 2 esté dispuesto a jugar una estrategia mixta (azar entre $C$ y $D$), la Esperanza Matemática (Expected Utility) de jugar $C$ debe ser *exactamente igual* a la Esperanza Matemática de jugar $D$. Si una fuera mayor que la otra, el Jugador 2 jugaría la mayor al 100% (estrategia pura).

Igualamos las utilidades esperadas del Jugador 2 dependientes de $p$ (la decisión del Jugador 1):
$$ E[U_2(C)] = p \cdot u(A,C) + (1-p) \cdot u(B,C) $$
$$ E[U_2(D)] = p \cdot u(A,D) + (1-p) \cdot u(B,D) $$

Al forzar $E[U_2(C)] = E[U_2(D)]$, obtenemos una ecuación lineal que nos permite despejar $p^*$.
*El resultado contraintuitivo de Nash*: ¡El Jugador 1 debe elegir su propia probabilidad $p$ basándose únicamente en los pagos del Jugador 2, no en los suyos propios! Su objetivo matemático es paralizar estratégicamente al oponente haciéndole dudar.

### Ejemplo: El Juego de la Inspección Fiscal
*   Fisco (Auditar con prob $p$, No auditar con prob `$1`-p$)
*   Ciudadano (Evadir con prob $q$, Pagar con prob `$1`-q$)
Si el ciudadano evade y es auditado, paga una multa altísima. Si el fisco audita a un ciudadano honesto, pierde el coste de la auditoría.
Resolviendo la ecuación de indiferencia, descubrimos que si aumentamos drásticamente la multa, ¡la probabilidad de que el fisco audite $p$ baja, pero la probabilidad de que el ciudadano evada $q$ se mantiene igual! Modificar los pagos de un jugador cambia el comportamiento (la probabilidad mixta) del *otro* jugador en el equilibrio.

## 7. Optimización de Equilibrios a Escala: Fictitious Play en GPU

Calcular equilibrios de Nash en juegos multijugador masivos (como 10,000 taxis decidiendo qué zona cubrir en el Gemelo Digital) es NP-Hard.
En ingeniería, usamos algoritmos de aprendizaje por refuerzo estocástico como **Fictitious Play**.

1. Inicializar todas las IA de los taxis con creencias aleatorias sobre lo que harán los demás.
2. Cada IA de taxi elige la mejor ruta (Estrategia Pura) maximizando su beneficio asumiendo que las creencias son ciertas.
3. Se observan las elecciones reales de todos.
4. Cada IA actualiza la "distribución de probabilidad histórica" de los demás.
5. Volver al paso 2.

```python
import numpy as np

def fictitious_play(payoff_matrix_1, payoff_matrix_2, iterations=10000):
    # payoff_matrix_1: Beneficios para el jugador 1. Shape (N_acciones_1, N_acciones_2)
    
    n_a1, n_a2 = payoff_matrix_1.shape
    
    # Historias de acciones (contadores empíricos de las estrategias mixtas)
    history_1 = np.ones(n_a1) / n_a1 
    history_2 = np.ones(n_a2) / n_a2
    
    for _ in range(iterations):
        # Cada jugador cree que el oponente usará su distribución histórica
        # Expected Utility = Matriz_Beneficios * Probabilidad_Oponente
        expected_u1 = payoff_matrix_1 @ (history_2 / np.sum(history_2))
        expected_u2 = payoff_matrix_2.T @ (history_1 / np.sum(history_1))
        
        # Best Response: Jugar la acción pura con máxima utilidad esperada
        best_response_1 = np.argmax(expected_u1)
        best_response_2 = np.argmax(expected_u2)
        
        # Actualizar creencias
        history_1[best_response_1] += 1
        history_2[best_response_2] += 1
        
    # Las estrategias mixtas convergen asintóticamente al Equilibrio de Nash
    nash_eq_1 = history_1 / np.sum(history_1)
    nash_eq_2 = history_2 / np.sum(history_2)
    
    return nash_eq_1, nash_eq_2
```
A medida que las iteraciones tienden a infinito, la distribución histórica de jugadas converge probabilísticamente a un Equilibrio de Nash en estrategias mixtas, permitiendo a los simuladores Cloud resolver juegos estocásticos masivos con convergencia garantizada (en juegos de suma cero o juegos de potencial).
