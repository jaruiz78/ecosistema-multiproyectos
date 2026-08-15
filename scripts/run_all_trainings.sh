#!/bin/bash
echo "=== INICIANDO PIPELINE DE ENTRENAMIENTO IA COMPLETO DEL ECOSISTEMA (64 VERTICALES + 20 CORES) ==="
for train_script in scripts/train_*.py; do
    if [ -f "$train_script" ]; then
        echo "-> Ejecutando: $train_script"
        python3 "$train_script"
    fi
done
echo "----------------------------------------------"
python3 scripts/litert_quantizer_pipeline.py
echo "=== TODOS LOS MODELOS GENERADOS, ENTRENADOS Y CUANTIZADOS LITERT EXITOSAMENTE ==="
