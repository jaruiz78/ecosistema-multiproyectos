#!/bin/bash
set -e

echo "=========================================================="
echo "🛡️  Antigravity 2.0: Agentic Router Local Validation"
echo "=========================================================="

cd /home/jaruiz/Desarrollo/agentic_router

echo "🔍 1. Instalando dependencias y limpiando..."
go mod tidy

echo "🧪 2. Ejecutando Pruebas (Zero-Mockito, Prove-It Standard)..."
go test ./internal/middleware/... -v

echo "⏱️ 3. Ejecutando Benchmarks (O(1) Concurrencia)..."
# Ejecutamos un benchmark para asegurar que no hay cuellos de botella por Thread Pinning
go test ./internal/middleware/... -bench=. -benchmem

echo "✅ Validación Local Exitosa."
echo "=========================================================="
