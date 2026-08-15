# Módulo 0 - Lección 4: Soberanía de Datos, GDPR, AI Act de la UE & Filtrado de PII

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es PII (Personally Identifiable Information)?
PII es cualquier dato que permita identificar de forma directa o indirecta a una persona física real. Ejemplos: NIF/NIE, nombre completo, email, número de teléfono, dirección IP, número de tarjeta de crédito o coordenadas GPS del domicilio.

### ¿Por qué es vital el cumplimiento legal (GDPR / AI Act)?
Las sanciones por violar el Reglamento General de Protección de Datos (GDPR) o el Reglamento de IA de la UE (AI Act) pueden alcanzar hasta 35 millones de euros o el 7% de la facturación global. **Nunca debemos enviar PII sin encriptar o sin anonimizar a modelos de IA ni guardarla en logs de texto plano.**

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Entrada de Datos no Confiable
        RAW[Texto de Usuario / Logs HTTP]
    end

    subgraph Middleware de Sanitización PII
        REGEX[Detector Regex de Patrones NIF/Email/Phone]
        HASH[Generador Hash Salteado SHA-256]
    end

    subgraph Destino Soberano (EU Region europe-west1)
        LOGS[Cloud Logging / Structured JSON]
        AI[Vertex AI / Gemini API EU Endpoint]
    end

    RAW --> REGEX
    REGEX -->|Detecta PII| HASH
    HASH -->|Sustituye por REDACTED_HASH| LOGS
    HASH -->|Datos Limpios| AI
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

### Sanitizador de PII en Java 25

```java
package com.corp.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class PiiSanitizer {

    private static final Pattern NIF_PATTERN = Pattern.compile("[0-9]{8}[A-Z]");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\+34|0034)?[679][0-9]{8}");

    public static String sanitize(String input) {
        if (input == null) return null;
        
        String clean = NIF_PATTERN.matcher(input).replaceAll(m -> anonymizeValue(m.group()));
        clean = EMAIL_PATTERN.matcher(clean).replaceAll(m -> anonymizeValue(m.group()));
        clean = PHONE_PATTERN.matcher(clean).replaceAll(m -> anonymizeValue(m.group()));
        
        return clean;
    }

    private static String anonymizeValue(String rawValue) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawValue.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return "[ANON_" + hexString.substring(0, 10) + "]";
        } catch (NoSuchAlgorithmException e) {
            return "[REDACTED]";
        }
    }
}
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Matriz de Cumplimiento Regulatorio

| Ley / Reglamento | Ámbito de Aplicación | Requisito Técnico Obligatorio | Impacto en Arquitectura |
| :--- | :--- | :--- | :--- |
| **GDPR (RGPD)** | Datos Personales de Ciudadanos UE | Derecho al Olvido / Pseudonimización | Hashing salteado SHA-256 en almacenamiento |
| **EU AI Act** | Sistemas de IA en la UE | Transparencia de Prompts / Cero Alucinaciones | Logging auditable de respuestas de Vertex AI |
| **Soberanía GCP** | Región `europe-west1` | In-Region Data Residency | Configuración estricta de KMS y Cloud Storage |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Imprimir excepciones completas en los logs que contienen el payload del usuario**:
   * *Síntoma*: `LOGGER.error("Error al procesar: " + userPayload)` escribe emails o tarjetas en Cloud Logging.
   * *Solución*: Pasa siempre los payloads por `PiiSanitizer.sanitize()` antes de escribir en log.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Soberanía de Datos, GDPR, AI Act de la UE & Filtrado de PII** a un estudiante de secundaria, **sin usar las palabras:** "Soberanía", "de", "Datos," ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
