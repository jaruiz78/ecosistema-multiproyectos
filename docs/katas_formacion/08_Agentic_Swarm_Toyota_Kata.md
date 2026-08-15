# 🥋 Kata 08: Orquestación de Enjambres Semánticos y Toyota Kata (Auto-Healing)

## 1. El Problema: Metaprogramación a Escala
En un ecosistema corporativo de 84 proyectos, modificar la arquitectura manualmente es inviable (anti-patrón de recursos). Sin embargo, delegar ciegamente la refactorización a un LLM en un bucle puede resultar en un **desastre de alucinaciones masivas**, donde un error de compilación se propaga exponencialmente, corrompiendo el código base.

## 2. La Solución: The Semantic Loop & Toyota Kata

El patrón *Semantic Loop* es una técnica de metaprogramación agéntica controlada que consta de tres pilares:
1. **Script Orquestador Híbrido**: Un script en Python que recorre los directorios y lee el estado real (AST) del código de forma asíncrona.
2. **Generación Aislada**: El script inyecta lógicas de negocio (usando plantillas inmutables) y despacha la invocación del agente LLM estrictamente para la formulación matemática o funcional.
3. **Toyota Kata (Límite de Reintentos)**: Se impone un límite matemático (máximo 3 iteraciones) de compilación fallida. Si un proyecto no logra pasar la suite `IntegrationTest` tras 3 intervenciones de la IA para auto-repararse, **se abandona** ese directorio, se aísla, y se avanza al siguiente.

## 3. Arquitectura del Bucle

### A. El Orquestador
El script (ej. `semantic_swarm_orchestrator.py`) no pide al LLM "escribe el código entero". Le pide: "Dado el dominio 'Drone Airspace', devuélveme los campos específicos y la fórmula de colisión en sintaxis Java".

### B. Inyección Segura (Zero-Mockito y Carrier Pinning)
La IA no debe modificar la infraestructura concurrente. El script orquestador envuelve siempre la respuesta matemática dentro de un bloque `ReentrantLock` y un `Record` estricto en Java 25.

### C. El Mecanismo de Auto-Healing (Fallo Elegante)
```python
intentos = 0
exito = False
while intentos < 3 and not exito:
    compilacion = ejecutar_comando("mvn clean test")
    if compilacion.exit_code == 0:
        exito = True
    else:
        # Pasa el error al LLM para que lo arregle.
        corregir_codigo(compilacion.stderr)
        intentos += 1

if not exito:
    loguear_fallo(directorio)
    revertir_git(directorio)
```

## 4. Ejercicio Práctico (Método Feynman)

> **Reto Feynman**: Explica a un niño de 12 años por qué no le decimos a la IA que arregle el código infinitas veces hasta que funcione, y por qué paramos a la tercera vez.

*Respuesta Feynman*: "Imagina que le pides a un amigo robot que arregle un reloj. Si el reloj sigue sin funcionar después de que el robot probó sus tres mejores herramientas, dejar que el robot siga probando cosas a lo loco probablemente termine rompiendo el reloj por completo. Es mejor que el robot se detenga, lo deje en la caja de 'relojes difíciles', y pase a arreglar los otros 83 relojes que sí sabe cómo arreglar. Al final del día, arregló 83 relojes en lugar de quedarse atascado horas rompiendo uno solo."
