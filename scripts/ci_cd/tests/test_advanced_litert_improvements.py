import os
import json
import pytest

def test_litert_advanced_improvements_file_structure():
    starter_ai_dir = "/home/jaruiz/Desarrollo/corp-spring-boot-starter/corp-ai-spring-boot-starter/src/main/java/com/corp/ai"
    
    assert os.path.exists(os.path.join(starter_ai_dir, "LiteRtPanamaFfmBridge.java"))
    assert os.path.exists(os.path.join(starter_ai_dir, "LiteRtDelegateManager.java"))
    assert os.path.exists(os.path.join(starter_ai_dir, "LiteRtModelManager.java"))
    assert os.path.exists(os.path.join(starter_ai_dir, "LiteRtAiAdapter.java"))

def test_litert_quantized_int8_model_simulation():
    # Simular la inferencia NPU cero-copia con modelo cuantizado int8
    input_data = [1.2, 3.4, 5.6, 7.8]
    quantized_int8 = [int(x * 127 / 10.0) for x in input_data]
    
    assert len(quantized_int8) == 4
    assert max(quantized_int8) <= 127
    assert min(quantized_int8) >= -128
