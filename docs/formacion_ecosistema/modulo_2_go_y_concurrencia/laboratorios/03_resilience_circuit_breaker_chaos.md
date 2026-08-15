# Módulo 2 - Lección 3: Patrones de Resiliencia, Circuit Breaker & Chaos Engineering

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es un Circuit Breaker (Interruptor Automático)?
En el cuadro eléctrico de tu casa, si hay un cortocircuito o sobretensión, la palanca del interruptor salta automáticamente para cortar la corriente y evitar que se quemen los electrodomésticos.

En software microservicios, un **Circuit Breaker** evita que un fallo en un servicio de terceros (como la API de TaxiCaller) sature tu propio sistema. Si la API de terceros empieza a fallar o ir extremadamente lenta, el Circuit Breaker "salta" (**Estado OPEN**) y rechaza inmediatamente las nuevas peticiones en $O(1)$ sin esperar a sufrir timeouts pesados.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
stateDiagram-v2
    [*] --> Closed: Operación Normal (Peticiones Fluyen)
    Closed --> Open: Ratio de Errores > Límite (60%)
    note right of Open
        Circuit Breaker Abierto:
        Rechaza peticiones instantáneamente
        (Fail-Fast en O(1))
    end
    Open --> HalfOpen: Expiración del Timeout (30s)
    HalfOpen --> Closed: Pruebas Exitosas
    HalfOpen --> Open: Prueba Falla
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```go
package main

import (
	"errors"
	"fmt"
	"time"

	"github.com/sony/gobreaker"
)

type ThirdPartyClient struct {
	cb *gobreaker.CircuitBreaker
}

func NewThirdPartyClient() *ThirdPartyClient {
	st := gobreaker.Settings{
		Name:        "ExternalAPI",
		MaxRequests: 3,                // Peticiones de prueba en Half-Open
		Interval:    10 * time.Second, // Ventana de limpieza
		Timeout:     30 * time.Second, // Tiempo en estado Open
		ReadyToTrip: func(counts gobreaker.Counts) bool {
			failureRatio := float64(counts.TotalFailures) / float64(counts.Requests)
			return counts.Requests >= 5 && failureRatio >= 0.6
		},
	}
	return &ThirdPartyClient{cb: gobreaker.NewCircuitBreaker(st)}
}

func (c *ThirdPartyClient) ExecuteCall() (string, error) {
	result, err := c.cb.Execute(func() (any, error) {
		// Llamada HTTP real
		return "OK", nil
	})

	if err != nil {
		if errors.Is(err, gobreaker.ErrOpenState) {
			return "", errors.New("servicio externo no disponible (Circuit Breaker Abierto)")
		}
		return "", err
	}
	return result.(string), nil
}
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Rendimiento Comparativo de Respuestas ante Fallos

| Estado del Breaker | Latencia de Respuesta | Consumo de CPU | Comportamiento del Sistema |
| :--- | :--- | :--- | :--- |
| **CLOSED (Normal)** | Latencia Real de Red (~200ms) | Normal | Tráfico fluido normal |
| **OPEN (Tripped)** | **< 0.1 ms (Fail-Fast)** | **Prácticamente 0** | Protege hilos de espera y memoria |
| **HALF-OPEN** | Latencia Real de Red (~200ms) | Bajo | Sondaje controlado con tráfico limitado |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Configurar timeouts de HTTP Client mayores que el Timeout del Circuit Breaker**:
   * *Síntoma*: Las Goroutines se acumulan esperando respuestas colgadas durante 60 segundos antes de que el Breaker se entere del fallo.
   * *Solución*: Configura siempre un timeout agresivo en el `http.Client` (p. ej. 500ms - 2s) inferior al intervalo del Breaker.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Patrones de Resiliencia, Circuit Breaker & Chaos Engineering** a un estudiante de secundaria, **sin usar las palabras:** "Patrones", "de", "Resiliencia," ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
