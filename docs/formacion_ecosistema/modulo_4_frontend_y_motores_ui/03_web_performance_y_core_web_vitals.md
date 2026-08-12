# Módulo 4.3: Web Performance y Core Web Vitals (CWV)

---

## 1. 🐣 Rincón Junior: ¿Por qué mi web es lenta?

Antiguamente, el rendimiento web se medía con el evento `window.onload`. Si la web terminaba de bajar todos los archivos en 2 segundos, "era rápida".
Pero esto era una mentira. Una web puede terminar de descargar en 2 segundos, pero mostrar una pantalla blanca durante 5 segundos porque está ejecutando un archivo JavaScript monstruoso que congela el navegador. O puede mostrar un botón en 1 segundo, pero cuando el usuario hace clic, el botón no responde porque la CPU está bloqueada.
Para medir la verdadera experiencia psicológica del usuario (lo que percibe el ojo humano y el dedo), Google inventó las métricas **Core Web Vitals**. Si suspendes este examen matemático, Google castigará tu web hundiéndola en los resultados de búsqueda (SEO Penalties).

---

## 2. 🔬 Fundamentos Arquitectónicos: La Tríada Core Web Vitals

Las tres métricas oficiales miden tres vectores fundamentales de la física de una página web: Carga, Interactividad y Estabilidad Visual.

1.  **LCP (Largest Contentful Paint)** - *Velocidad de Carga Visible*.
    *   **¿Qué mide?** El tiempo exacto (en milisegundos) que tarda en renderizarse el bloque de contenido (imagen o texto) **más grande** de la pantalla inicial (Hero Image, Título Principal).
    *   **La nota de corte matemática**: Debe ocurrir en $< 2.5$ segundos. Si tarda $> 4.0$ segundos, suspendes.
    
2.  **INP (Interaction to Next Paint)** - *Interactividad y Bloqueo de Hilo*.
    *   **¿Qué mide?** La latencia. Si tocas un botón de acordeón, ¿cuánto tarda el navegador en pintar el cambio visual en la pantalla? 
    *   Reemplazó a FID. Mide la **peor** interacción del usuario durante toda la vida de la página. Si tu JavaScript tiene un bucle matemático gigante y bloquea el Main Thread, el clic del usuario será ignorado hasta que el bucle acabe.
    *   **La nota de corte**: $< 200$ milisegundos para ser Bueno.
    
3.  **CLS (Cumulative Layout Shift)** - *Estabilidad Visual Geométrica*.
    *   **¿Qué mide?** Los saltos de la pantalla. Estás leyendo un artículo, carga un anuncio publicitario encima del texto, y el texto salta 100 píxeles hacia abajo. Haces clic accidentalmente en el anuncio.
    *   Es un cálculo físico: `Fracción de Impacto (área de la pantalla afectada) * Fracción de Distancia (cuánto saltó)`.
    *   **La nota de corte**: $< 0.1$ para ser Bueno. Cero absoluto es la meta.

---

## 3. 🚀 Arquitectura Práctica: Optimizando el LCP y el Critical Rendering Path

Para lograr un LCP en $<2.5$ segundos en redes móviles 3G, debemos dominar el **Camino Crítico de Renderizado (Critical Rendering Path)**.

El navegador funciona así:
1. Baja el HTML.
2. Ve un `<link rel="stylesheet" href="style.css">`.
3. **¡ALERTA ROJA!** El CSS es un "Render-Blocking Resource". El navegador detiene la construcción de la pantalla en seco. Nunca dibujará un solo píxel hasta haber bajado el 100% del CSS (para evitar que la web se vea fea por un segundo (FOUC)).

**Técnicas de Ingeniería**:
*   **Critical CSS**: Extraer usando IA o scripts los 10 KB de código CSS que dan estilo **solo a la parte visible sin hacer scroll** (Above-the-fold). Incrustar ese CSS directamente dentro del HTML (`<style>`). Retrasar la carga asíncrona del archivo `style.css` completo. Esto permite al navegador pintar la pantalla inicial en el milisegundo 1.
*   **Preload de la Imagen LCP**: Si tu LCP es una foto gigante, el navegador no la descubrirá hasta muy tarde. Obligamos al navegador a robar ancho de banda desde el milisegundo cero añadiendo `<link rel="preload" href="hero.jpg" as="image">` en el `<head>`.

---

## 4. 🧠 Internals Avanzados: Derrotando el INP y el CLS

### Solución SRE para INP (Yielding to the Main Thread)
JavaScript es un lenguaje de **Un Solo Hilo** (Single Threaded). Si tienes que ordenar una lista de 50,000 elementos, y tu bucle `for` tarda 300ms, el hilo principal está secuestrado. Cualquier clic del usuario será ignorado, generando un INP desastroso.
*   **La Arquitectura (Yielding)**: Debes partir las tareas largas matemáticas (Long Tasks > 50ms). Si usas React 18, Fiber hace esto solo (Concurrent Mode). Si escribes Vanilla JS, debes usar `setTimeout(func, 0)` o la nueva API `scheduler.yield()` cada 20 milisegundos de bucle para devolver el control al navegador, permitirle procesar los clics del usuario, y luego continuar el trabajo pesado.
*   **Web Workers**: Mandar el cálculo de los 50,000 elementos a un hilo físico de CPU separado (Background Thread) usando la API de Web Workers, dejando el hilo de la UI limpio y al 0% de uso.

### Solución Matemática para CLS (Aspect-Ratio)
Los saltos de pantalla (CLS) ocurren porque el navegador no sabe el alto (height) de una imagen antes de descargarla.
En los 90 se ponía `<img width="800" height="600">`. Con el diseño Responsive, la imagen se adapta al móvil, y el `width="800"` se rompe.
*   **La Solución (CSS `aspect-ratio`)**: Los navegadores modernos usan la división matemática de los atributos. Si pones `width="800" height="600"`, el navegador sabe antes de descargar que la proporción es `4:3`. Calcula inmediatamente la caja reservada en pantalla. Cuando la foto de 3 Megabytes termine de cargar 4 segundos después, encajará perfectamente en el hueco reservado. Cero desplazamientos. CLS = 0.0.

---

## 5. ⚠️ Runbook SRE: Auditoría de Third-Party Scripts

**Incidente**: Has optimizado tu código React al límite, tu servidor responde en 10ms. Pero el LCP sigue siendo 5 segundos.

**Diagnóstico Arquitectónico**:
El departamento de Marketing ha inyectado 15 scripts de terceros en Google Tag Manager (Hotjar, Facebook Pixel, Intercom). Estos scripts pesan Megabytes, bloquean el Hilo Principal y compiten por el ancho de banda con tus recursos críticos.

**Solución Arquitectónica (Sandboxing y PartyTown)**:
1. Nunca permitir la carga síncrona de scripts de terceros. Usar `defer` (ejecuta tras construir el DOM) o `async`.
2. Para empresas Enterprise, usar arquitecturas como **Partytown**. Esta librería usa Service Workers para atrapar los scripts de Marketing (Hotjar, Pixel) y ejecutarlos a la fuerza dentro de un **Web Worker** aislado (Background Thread). Marketing obtiene sus métricas y su código se ejecuta, pero el Hilo Principal de la aplicación queda 100% inmune, salvando el Core Web Vitals matemáticamente.

---
---

# 🛑 [DEEP-DIVE] Render Pipeline y Optimizaciones V8 (Turbofan)

Para comprender el rendimiento a un nivel arquitectónico profundo, hay que analizar cómo el navegador web convierte HTML/JS en píxeles (El Render Pipeline) y cómo el motor JavaScript (V8 en Chrome/Node) compila y ejecuta las instrucciones, afectando la latencia final.

## 6. El Pipeline de Renderizado del Navegador

El proceso estricto desde el Byte hasta el Píxel consta de 5 fases críticas:
1. **DOM / CSSOM Construction**: 
   - El parser de HTML construye el Document Object Model (DOM).
   - Paralelamente, el CSS Parser construye el CSS Object Model (CSSOM).
2. **Render Tree Construction**: 
   - El motor de layout une el DOM y el CSSOM. Excluye explícitamente los nodos `display: none` o `<head>` (ya que no tienen representación visual), resultando en el *Render Tree*.
3. **Layout (Reflow)**: 
   - El motor matemático (Blink) recorre el *Render Tree* y calcula la geometría exacta (ancho, alto, X, Y) de cada nodo en función del Viewport actual. Es un proceso polinómico recurrente porque el tamaño del hijo afecta al padre.
   - *Nota de Rendimiento*: Evita forzar reflujos sincrónicos interactuando entrelazadamente con escrituras (`element.style.width = '100px'`) y lecturas (`element.offsetWidth`) en el mismo bucle JS (Layout Thrashing).
4. **Paint**: 
   - Crea un registro de dibujo (Display List) con instrucciones (ej. dibuja un rectángulo, dibuja el texto).
5. **Compositing**: 
   - Transforma las listas de comandos en píxeles. En motores modernos, los elementos que tienen propiedades específicas (ej. `transform: translate3d(...)` o `opacity`) se promueven a *Compositing Layers* separadas que se procesan directamente en la memoria de la Tarjeta Gráfica (GPU). Al alterar solo `transform` u `opacity` (animaciones), Chrome salta las fases de Layout y Paint, limitando el esfuerzo exclusivamente a la fase de Compositing (Coste $\approx 0$ en CPU).

## 7. Motor V8: Ignition, Turbofan y Hidden Classes

El tiempo de ejecución (INP) de tu JavaScript está determinado por cómo el V8 (Motor de Chrome) ingiere tu código.
1. **Ignition (Intérprete)**: Compila JS crudo a Bytecode genérico de rápida ejecución inicial.
2. **Turbofan (JIT Compiler)**: Observa el Bytecode en ejecución (Profiling). Si una función matemática (ej. `calculateDistance(a, b)`) se llama miles de veces (es "caliente" o "hot"), Turbofan la compila a lenguaje de máquina hiper-optimizado (AOT behavior en JIT).

**El Problema de las Polimorfismos (Hidden Classes / Shapes)**:
JavaScript no tiene tipos estrictos compilados. Para que Turbofan genere código de máquina de rendimiento nativo (C++), asume que los objetos pasados a una función tendrán siempre la misma estructura matemática (Shape o Hidden Class).
```javascript
// Si hacemos esto:
const obj1 = { x: 1, y: 2 }; 
const obj2 = { x: 3, y: 4, z: 5 }; // Tiene una Shape distinta (z)

function add(point) {
    // Si recibe obj1, el V8 compila una versión rápida.
    // Si recibe obj2, el V8 desoptimiza (Deoptimization / Bailout) la función
    // y retrocede al intérprete lento (Ignition).
    return point.x + point.y; 
}
```
**Estrategia SRE**: Inicializa todos los atributos de tus clases y objetos JavaScript siempre en el mismo orden (ej. en constructores) y no modifiques dinámicamente el diccionario de clases (`delete obj.x`). Esto garantiza *Monomorphic Functions*, lo que permite a Turbofan y al recolector de basura (Orinoco) alcanzar un throughput similar al de C++, eliminando los micro-bloqueos del hilo principal (Janks) que arruinan la métrica INP.
