#!/usr/bin/env bash
# start_emulators.sh
# Script to launch the GCP & Firebase emulators via docker-compose

echo "🚀 Iniciando infraestructura local de emuladores (Zero-Cost Local Reality)..."

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_ROOT" || exit 1

# Start the emulators using docker-compose
docker-compose -f scripts/docker-compose.emulators.yml up -d

echo "✅ Emuladores iniciados correctamente."
echo "- Firebase Emulator UI: http://localhost:4000"
echo "- Firestore Emulator: localhost:8080"
echo "- Pub/Sub Emulator: localhost:8085"
echo "- Ollama (Local AI): localhost:11434"
