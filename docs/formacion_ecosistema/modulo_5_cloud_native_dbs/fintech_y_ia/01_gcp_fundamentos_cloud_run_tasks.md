# Módulo 5 - Lección 1: Fundamentos de GCP (Cloud Run, Cloud Tasks & Firestore) desde Cero

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué son Cloud Run, Cloud Tasks y Firestore?
Imagina un parque de atracciones:
* **Cloud Run**: Las atracciones que se encienden solo cuando entra una persona y se apagan automáticamente cuando no hay nadie (**Escalado a Cero**).
* **Cloud Tasks**: El empleado de la entrada organizando la fila de personas repartiendo tickets en orden para que la atracción no se desborde.
* **Firestore**: El archivo central NoSQL en la nube guardando instantáneamente la ficha de cada visitante sin requerir servidores de base de datos tradicionales.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Cliente Web / Dashboard
        CLIENT[React PWA / Flutter App]
    end

    subgraph GCP Serverless Compute
        CR[Cloud Run Service Java/Go]
        CT[Cloud Tasks Queue]
        WORKER[Cloud Run Worker Background]
    end

    subgraph Almacenamiento & IAM
        FS[(Firestore Multi-Tenant DB)]
        IAM[GCP IAM Least Privilege]
    end

    CLIENT -->|HTTPS Request| CR
    CR -->|Encola proceso pesado| CT
    CT -->|HTTP Post Task| WORKER
    WORKER -->|Aislamiento Tenant| FS
    FS --- IAM
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```bash
# Despliegue en Cloud Run con escalado a cero
gcloud run deploy saas-backend-service \
    --image=europe-west1-docker.pkg.dev/saas-prod/repo/service:latest \
    --region=europe-west1 \
    --platform=managed \
    --min-instances=0 \
    --max-instances=10 \
    --concurrency=80
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Cloud Run Concurrency vs CPU Allocation

| Configuración GCP | Comportamiento de Instancia | Impacto en Costes |
| :--- | :--- | :--- |
| `--no-cpu-throttling` | CPU dedicada 100% activa fuera de peticiones | Mayor coste / Ideal para tareas background |
| `concurrency = 80` | Procesamiento de 80 peticiones concurrentes por contenedor | **Máxima eficiencia FinOps / Menor coste** |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Guardar archivos en el sistema de archivos local `/tmp` del contenedor Cloud Run asumiendo persistencia**:
   * *Síntoma*: Los archivos desaparecen cuando Cloud Run escala a cero la instancia.
   * *Solución*: Utiliza siempre **Google Cloud Storage (GCS)** o Firestore para persistir archivos o estado.
