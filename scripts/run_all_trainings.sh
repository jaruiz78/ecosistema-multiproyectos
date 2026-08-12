#!/bin/bash
echo "=== INICIANDO PIPELINE DE ENTRENAMIENTO IA ==="
python3 scripts/train_dqn_mobility.py
echo "----------------------------------------------"
python3 scripts/train_pinn_water.py
echo "----------------------------------------------"
python3 scripts/train_nsga2_energy.py
echo "----------------------------------------------"
python3 scripts/train_federated_b2g.py
echo "=== TODOS LOS MODELOS GENERADOS ==="
