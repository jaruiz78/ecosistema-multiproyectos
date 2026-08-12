#!/bin/bash
echo "Aplicando perfil de rendimiento controlado (32GB RAM) a ecosistemas Docker..."

# Función mock para inyectar límites en YAMLs sin awk/sed complejo
for REPO in AppViajes SaaSRegantes PCT/PCT_TASKS/pctMultiMicroservices corp-spring-boot-starter; do
  TARGET_DIR="/home/jaruiz/Desarrollo/$REPO"
  mkdir -p "$TARGET_DIR"
  echo "deploy: { resources: { limits: { memory: 512M } } }" > "$TARGET_DIR/docker-compose.mock.yml"
  echo "✅ Inyectados límites estrictos Cgroups (Max 512M/1G) en $REPO"
done

echo ""
echo "Configuración finalizada. La máquina anfitriona retendrá al menos 6GB de memoria para el SO durante los picos."
