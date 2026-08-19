# Módulo 1 - Lección 5: Observabilidad con OpenTelemetry (OTEL) & Semantic Conventions

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es la Observabilidad y los tres pilares (Métricas, Logs y Trazas)?
Imagina que eres un médico cuidando a un paciente.
* **Logs (Registros)**: Las notas escritas en el historial ("El paciente estornudó a las 10:00 AM").
* **Métricas**: Las constantes vitales continuas (frecuencia cardíaca 72 bpm, temperatura 36.5 ºC).
* **Trazas (Traces)**: Un tinte fluorescente que sigues a través de las venas del paciente para ver exactamente por qué órgano pasa y dónde se detiene la sangre.

**OpenTelemetry (OTEL)** es el estándar abierto de la industria para generar y conectar estos tres datos con un identificador único (**TraceID**).

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Petición HTTP / RPC Entrante
        REQ[Cliente API]
    end

    subgraph Instrumentación OpenTelemetry (OTEL)
        TRACER["Tracer / Generador de Spans"]
        LOGS[JSON Structured Logger]
        METRICS["Meter / Metrics Collector"]
    end

    subgraph Plataforma Observabilidad GCP / Cloud Trace
        TRACE_SYS["Cloud Trace / Jaeger"]
        LOG_SYS[Cloud Logging]
    end

    REQ --> TRACER
    TRACER -->|TraceId / SpanId| LOGS
    TRACER -->|Histograma de Latencia| METRICS
    TRACER --> TRACE_SYS
    LOGS --> LOG_SYS
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

### Instrumentación de un Span de Telemetría en Java

```java
package com.corp.telemetry;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.stereotype.Service;

@Service
public class OrderTelemetryService {

    private final Tracer tracer = GlobalOpenTelemetry.getTracer("com.corp.orders", "1.0.0");

    public void processPaymentWithTelemetry(String orderId, String tenantId) {
        // Crear un nuevo Span de trazado con nombre descriptivo
        Span span = tracer.spanBuilder("ProcessPayment")
                .setAttribute("order.id", orderId)
                .setAttribute("tenant.id", tenantId)
                .startSpan();

        // Scope asegura que los logs internos reciban automáticamente el TraceId
        try (Scope scope = span.makeCurrent()) {
            executePaymentLogic();
        } catch (Exception e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end(); // Se debe cerrar siempre el Span
        }
    }

    private void executePaymentLogic() {
        // Lógica de pago
    }
}
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Convenciones Semánticas de Atributos OTEL (OTEL SemConv)

| Atributo Semántico | Ejemplo de Valor | Propósito |
| :--- | :--- | :--- |
| `http.request.method` | `"POST"` | Método HTTP estándar |
| `http.response.status_code` | `200` | Código de respuesta HTTP |
| `db.system` | `"postgresql"` | Tipo de motor de base de datos |
| `tenant.id` | `"tenant-valencia-01"` | Identificador de inquilino Multi-Tenant |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Olvidar cerrar un Span (`span.end()`) en el bloque `finally`**:
   * *Síntoma*: Fuga de memoria en el buffer de telemetría y trazas "infinitas" colgadas en Cloud Trace.
   * *Solución*: Utiliza siempre la estructura `try-finally` o `try-with-resources` para garantizar `span.end()`.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Observabilidad con OpenTelemetry (OTEL) & Semantic Conventions** a un estudiante de secundaria, **sin usar las palabras:** "Observabilidad", "con", "OpenTelemetry" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.

---

## ⚙️ Primeros Principios & Fundamentos Conceptuales
1. **Descomposición Atómica:** Cada componente en Módulo 1 - Lección 5: Observabilidad con OpenTelemetry (OTEL) & Semantic Conventions se modela de forma determinista y sin estado mutable compartido.
2. **Invariante de Dominio:** Los estados del sistema transicionan exclusivamente a través de funciones puras e interfaces selladas.
3. **Cero Suposiciones:** No se asume fiabilidad de red ni memoria infinita; cada llamada maneja explícitamente fallos y límites de cuota.

