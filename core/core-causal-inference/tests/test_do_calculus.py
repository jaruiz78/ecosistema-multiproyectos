import sys
import os
import pytest

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from do_calculus import CausalInferenceEngine

def test_causal_inference_effect_estimation():
    engine = CausalInferenceEngine(confidence_alpha=0.05)
    
    treatment_val = 10.0
    baseline_state = 50.0
    confounder_adj = 2.0
    
    result = engine.estimate_do_treatment_effect(treatment_val, baseline_state, confounder_adj)
    
    # Expected causal_effect: (10.0 * 1.15) - (2.0 * 0.20) = 11.5 - 0.4 = 11.1
    assert pytest.approx(result["causal_effect"], 0.001) == 11.1
    assert pytest.approx(result["expected_outcome"], 0.001) == 61.1
    assert result["statistically_significant"] is True

def test_causal_inference_zero_treatment():
    engine = CausalInferenceEngine(confidence_alpha=0.01)
    result = engine.estimate_do_treatment_effect(0.0, 100.0, 0.0)
    assert result["causal_effect"] == 0.0
    assert result["expected_outcome"] == 100.0
    assert result["statistically_significant"] is True
