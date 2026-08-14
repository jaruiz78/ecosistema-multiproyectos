"""
Arquitectura y especificación formal para gpu_health_inspector.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
"""
gpu_health_inspector.py
-------------------------------------------------------------------------
Inspector Térmico Pasivo y Monitoreo de VRAM para NVIDIA GPU (RTX 5060).
Alerta si la temperatura supera los 75°C o la memoria VRAM supera el 90%.
-------------------------------------------------------------------------
"""
import subprocess
import sys

def check_gpu_health() -> dict:
    try:
        cmd = "nvidia-smi --query-gpu=temperature.gpu,memory.used,memory.total,power.draw --format=csv,noheader,nounits"
        res = subprocess.run(cmd, shell=True, capture_output=True, text=True)
        if res.returncode == 0:
            parts = [p.strip() for p in res.stdout.strip().split(",")]
            temp = float(parts[0])
            mem_used = float(parts[1])
            mem_total = float(parts[2])
            power = float(parts[3]) if len(parts) > 3 else 0.0
            
            mem_pct = (mem_used / mem_total) * 100.0
            
            status = "HEALTHY"
            warning = None
            if temp > 75.0:
                status = "WARNING_HIGH_TEMP"
                warning = f"Temperatura GPU elevada: {temp}°C (Tope: 75°C)"
            elif mem_pct > 90.0:
                status = "WARNING_HIGH_VRAM"
                warning = f"Uso de VRAM elevado: {mem_pct:.1f}% ({mem_used:.0f}/{mem_total:.0f} MB)"
                
            return {
                "status": status,
                "temperature_c": temp,
                "memory_used_mb": mem_used,
                "memory_total_mb": mem_total,
                "memory_pct": mem_pct,
                "power_draw_w": power,
                "warning": warning
            }
    except Exception as e:
        return {"status": "ERROR", "warning": str(e)}
        
    return {"status": "UNKNOWN", "warning": "NVIDIA GPU not accessible"}

if __name__ == "__main__":
    health = check_gpu_health()
    print("========================================================")
    print("  INSPECTOR TÉRMICO Y VRAM GPU LOCAL (NVIDIA RTX 5060)")
    print("========================================================")
    print(f"  -> Estado Global      : {health['status']}")
    print(f"  -> Temperatura GPU    : {health.get('temperature_c', 0.0)}°C")
    print(f"  -> Memoria VRAM Usada : {health.get('memory_used_mb', 0.0):.0f} MB / {health.get('memory_total_mb', 0.0):.0f} MB ({health.get('memory_pct', 0.0):.1f}%)")
    print(f"  -> Consumo de Poder   : {health.get('power_draw_w', 0.0)} W")
    if health.get("warning"):
        print(f"  ⚠️ Alerta: {health['warning']}")
    else:
        print("  ✓ GPU Operando en Rango Normal Secundario")
