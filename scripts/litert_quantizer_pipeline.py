#!/usr/bin/env python3
"""
litert_quantizer_pipeline.py
-------------------------------------------------------------------------
Pipeline industrializado de cuantización y empaquetado para Edge LiteRT.
Convierte pesos, tablas Q y modelos entrenados (.pkl) a tensores cuantizados
(INT8 / FP16) compatibles con el motor LiteRT y OffHeapTensorBufferPool.
-------------------------------------------------------------------------
"""
import os
import sys
import pickle
import json
import numpy as np

def quantize_float_to_int8(data_array):
    """Cuantiza un array de flotantes a INT8 con escala y zero-point."""
    arr = np.array(data_array, dtype=np.float32)
    min_val = float(np.min(arr))
    max_val = float(np.max(arr))
    if max_val == min_val:
        scale = 1.0
        zero_point = 0
        quantized = np.zeros_like(arr, dtype=np.int8)
    else:
        scale = (max_val - min_val) / 255.0
        zero_point = int(np.round(-min_val / scale) - 128)
        quantized = np.clip(np.round(arr / scale) + zero_point, -128, 127).astype(np.int8)
    return {
        "quantized_bytes_b64": quantized.tobytes().hex(),
        "scale": round(scale, 6),
        "zero_point": zero_point,
        "original_shape": list(arr.shape),
        "dtype": "INT8"
    }

def process_model_quantization(model_path, output_dir):
    """Procesa un archivo de modelo y genera su versión cuantizada LiteRT."""
    with open(model_path, 'rb') as f:
        model = pickle.load(f)
        
    model_name = os.path.basename(model_path).replace('.pkl', '')
    quantized_manifest = {
        "model_name": model_name,
        "format": "LiteRT_Edge_Tensor_v1",
        "timestamp": "2026-08-14T10:00:00Z",
        "metadata": model.get("metadata", "LiteRT Quantized Artifact"),
        "tensors": {}
    }
    
    # Extraer pesos numéricos
    if "q_table" in model and isinstance(model["q_table"], dict):
        q_matrix = np.array(list(model["q_table"].values()))
        quantized_manifest["tensors"]["q_table"] = quantize_float_to_int8(q_matrix)
    elif "weights" in model:
        quantized_manifest["tensors"]["weights"] = quantize_float_to_int8(model["weights"])
    else:
        # Extraer cualquier clave con arrays o floats
        flat_vals = []
        for k, v in model.items():
            if isinstance(v, (int, float)):
                flat_vals.append(v)
            elif isinstance(v, list) and all(isinstance(x, (int, float)) for x in v):
                flat_vals.extend(v)
        if flat_vals:
            quantized_manifest["tensors"]["primary_params"] = quantize_float_to_int8(flat_vals)
            
    out_file = os.path.join(output_dir, f"{model_name}.litert.json")
    with open(out_file, 'w', encoding='utf-8') as f:
        json.dump(quantized_manifest, f, indent=2)
        
    return out_file

def run_quantization_pipeline():
    print("==========================================================")
    print("📦 INICIANDO PIPELINE DE CUANTIZACIÓN EDGE LITERT (INT8)")
    print("==========================================================")
    
    models_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "../data/models"))
    edge_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "../data/models/litert"))
    os.makedirs(edge_dir, exist_ok=True)
    
    pkl_files = [f for f in os.listdir(models_dir) if f.endswith('.pkl')]
    if not pkl_files:
        print("⚠️ No se encontraron archivos .pkl en data/models. Entrena primero los modelos.")
        return False
        
    success_count = 0
    for pkl_file in sorted(pkl_files):
        full_path = os.path.join(models_dir, pkl_file)
        try:
            out_file = process_model_quantization(full_path, edge_dir)
            size_bytes = os.path.getsize(out_file)
            print(f"  -> Cuantizado: {pkl_file:32s} => {os.path.basename(out_file):35s} ({size_bytes} bytes)")
            success_count += 1
        except Exception as e:
            print(f"  ❌ Error cuantizando {pkl_file}: {e}")
            
    print(f"\n✅ Total modelos cuantizados para Edge LiteRT: {success_count}/{len(pkl_files)}")
    print("==========================================================")
    return True

if __name__ == '__main__':
    run_quantization_pipeline()
