#!/bin/bash
echo "=== INICIANDO VALIDACIÓN DE SIMULACIONES CON IA ==="
python3 simulate_logistica.py
echo "---------------------------------------------------"
python3 simulate_energia.py
echo "---------------------------------------------------"
python3 simulate_b2g.py
echo "---------------------------------------------------"
python3 simulate_tokenrwa.py
echo "---------------------------------------------------"
python3 simulate_defensa.py
echo "---------------------------------------------------"
python3 simulate_vpp.py
echo "---------------------------------------------------"
python3 simulate_circular.py
echo "=== SIMULACIONES FINALIZADAS ==="
