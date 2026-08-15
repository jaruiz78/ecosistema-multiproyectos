# 🚀 Getting Started - Ecosistema Corporativo

Bienvenido al Ecosistema Corporativo de Desarrollo Agéntico. Esta guía te permitirá tener todo el entorno levantado y funcional en menos de **10 minutos**.

## 📋 Requisitos Previos

Asegúrate de tener instalados los siguientes componentes base:
- **Go** (>= 1.22)
- **Java** (>= 25) y Maven (>= 3.9)
- **Node.js** (>= 20) y npm
- **Flutter** (>= 3.24)
- **Docker** y Docker Compose
- **Python** (>= 3.12) con `pip`
- **Ollama** (con modelos: `deepseek-r1:8b`, `qwen2.5-coder:7b`, `gemma3:4b`)

## ⚡ Quickstart (Paso a Paso)

### 1. Clonar y Preparar el Entorno (2 min)
```bash
cd /home/jaruiz/Desarrollo
# Iniciar servicios de infraestructura en Docker (Bases de datos locales, redis, emuladores GCP)
bash scripts/start_emulators.sh
```

### 2. Levantar el Backend y Microservicios (3 min)
Ejecuta el script maestro que orquesta Java (Spring Boot) y Go (BFF):
```bash
# Compilar core starter y microservicios
python3 scripts/run_master_e2e_ecosystem_integration_test.py
```
*Si todos los pasos muestran `PASSED`, los backends están listos.*

### 3. Levantar el Frontend SaaSRegantes (React) (2 min)
```bash
cd SaaSRegantes/frontend/farmer-pwa
npm install
npm run dev
```
Accede a `http://localhost:5173` para ver el dashboard.

### 4. Lanzar AppViajes (Mobile Flutter) (3 min)
Asegúrate de tener un emulador Android/iOS corriendo o un dispositivo conectado.
```bash
cd AppViajes/services/mobile-app
flutter pub get
flutter run
```

## 🧠 Herramientas Agénticas y Consilium Romano

Para validar arquitecturas antes de hacer commit, utiliza nuestro tribunal de IA (Consilium Romano):
```bash
# Audita tus cambios con 3 modelos IA diferentes
python3 scripts/consilium_romano_tribunal.py --check-all
```

## 📚 Documentación de Referencia

- **Diseño del Ecosistema y Agentes:** [AGENTS.md](./AGENTS.md)
- **API Consolidada:** [openapi_consolidated.yaml](./openapi_consolidated.yaml)
- **Currículum de Facultades:** [docs/formacion_ecosistema/](./formacion_ecosistema/)

¡Y ya está! Tienes la base corporativa y las aplicaciones de dominio corriendo con cero costes de cloud utilizando dependencias locales simuladas.
