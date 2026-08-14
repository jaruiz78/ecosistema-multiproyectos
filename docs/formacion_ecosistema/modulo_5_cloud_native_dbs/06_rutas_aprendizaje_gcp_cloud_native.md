# 6. Rutas de Aprendizaje Práctico: Google Cloud (GCP) & Infraestructura Inmutable

Este documento presenta las **vías formativas más robustas, gratuitas y reconocidas** de la industria para consolidar arquitecturas puramente Cloud-Native. Pone en práctica la teoría del Módulo 5 (Contenedores Linux, gVisor, Kubernetes Internals) permitiendo al ingeniero orquestar despliegues elásticos, masivos y de confianza cero (Zero-Trust) mediante Infraestructura como Código (IaC).

## 1. El Catálogo Central de Google Cloud (GCP)

### A. Google Cloud Skills Boost (Antiguo Qwiklabs)
La plataforma inmersiva y práctica (Hands-on labs) oficial de Google.
- **Enfoque:** Despliegues en Google Kubernetes Engine (GKE), Google Cloud Run (Serverless), Cloud Build (CI/CD) e Identity and Access Management (IAM).
- **Acceso:** [Google Cloud Skills Boost](https://www.cloudskillsboost.google/) (Especialmente los *Quest* gratuitos y *Partner learning paths* si aplica).
- **Rigor:** Permite operar consolas y terminales reales en GCP. El ingeniero practica el modelo de Menor Privilegio (Principle of Least Privilege) asignando *Service Accounts* sin claves en código.

### B. Google Cloud Architecture Center
La enciclopedia de los diseños validados.
- **Enfoque:** Patrones de arquitectura de referencia (Ej: "Arquitecturas Serverless multi-tenant", "Recuperación ante desastres (DR) en GCP", "Procesamiento de eventos en tiempo real con Pub/Sub y Dataflow").
- **Acceso:** [GCP Architecture Center](https://cloud.google.com/architecture).
- **Rigor:** Imprescindible antes de provisionar infraestructura. Previene el "sobre-ingeniería" (Over-engineering), guiando al diseño de sistemas con un coste marginal tendente a cero ($<0.015$ USD/MAU/mes).

## 2. Infraestructura como Código (IaC) e Inmutabilidad

### C. Terraform (HashiCorp) & Official Tutorials
El estándar de facto para la Infraestructura como Código agnóstica.
- **Enfoque:** Declaración de estados, provisionamiento de *Cloud SQL*, *BigQuery* y VPCs en Google Cloud mediante scripts HCL. Control de *state locks* distribuidos.
- **Acceso:** [HashiCorp Learn: Terraform GCP](https://developer.hashicorp.com/terraform/tutorials/gcp-get-started).
- **Rigor:** Establece la inmutabilidad absoluta de la infraestructura corporativa. Todo cambio en GCP debe nacer de un *pull request* revisado que actualiza el estado de Terraform. Prohibido el *ClickOps*.

## 3. Seguridad Nativa y Orquestación

### D. Kubernetes.io & KubeAcademy (VMware)
Recursos exhaustivos para cuando el Serverless (Cloud Run) se queda corto y se necesita control extremo de clústeres.
- **Enfoque:** Configuración de manifiestos YAML, StatefulSets (para bases de datos estáticas), Network Policies, ingress controllers y escalado HPA (Horizontal Pod Autoscaler).
- **Acceso:** [Kubernetes Official Docs](https://kubernetes.io/docs/home/) y [KubeAcademy](https://kube.academy/).
- **Rigor:** Garantiza que el ingeniero entienda las abstracciones sobre los nodos físicos y evite el antipatrón de crear múltiples clústeres pequeños (preferencia por grandes clústeres particionados por *namespaces* lógicos).

## 4. Analítica y Automatización 

### E. GitHub Actions & GCP Workload Identity Federation
Para cerrar el ciclo de vida del software (SDLC) con despliegues invisibles.
- **Enfoque:** Configuración de CI/CD, inyección de credenciales temporales asíncronas (Workload Identity sin secretos de larga duración) y despliegue Canary progresivo.
- **Acceso:** [GitHub Docs (OIDC with GCP)](https://docs.github.com/en/actions/deployment/security-hardening-your-deployments/configuring-openid-connect-in-google-cloud-platform).
- **Rigor:** Punto crítico de SRE. Nadie posee claves maestras, las integraciones temporales duran lo que el token OIDC, eliminando fugas en repositorios masivos.

---

> **Objetivo de Competencia:** Al completar esta matriz de recursos, el Cloud Engineer podrá orquestar clústeres y topologías sin servidor que escalen dinámicamente de `$0` \to 10,000$ peticiones concurrentes, inyectando infraestructura inmutable que cumpla con los estándares europeos soberanos (Compliance GDPR y Cloud Sovereignty).
