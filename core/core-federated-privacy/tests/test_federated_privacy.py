"""
Arquitectura y especificación formal para test_federated_privacy.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-005-slsa-l3-cosign-provenance.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/04_compliance_gdpr_ai_act_pii.md
- Referencia Académica: Dwork (2006) Differential Privacy; Zero-Trust Architecture (NIST 800-207)
"""
import unittest
import os
import sys
import numpy as np

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '../src')))
from federated_privacy_engine import FederatedPrivacyEngine

class TestFederatedPrivacyEngine(unittest.TestCase):
    def setUp(self):
        self.engine = FederatedPrivacyEngine(epsilon=1.0, clip_norm=1.5)

    def test_aggregate_gradients_federated(self):
        client_1 = np.array([1.0, 2.0, -1.0])
        client_2 = np.array([0.5, 1.5, -0.5])
        client_3 = np.array([1.2, 1.8, -0.8])

        agg = self.engine.aggregate_gradients_federated([client_1, client_2, client_3])
        self.assertEqual(agg.shape, (3,))
        self.assertFalse(np.isnan(agg).any())

    def test_privatize_telemetry_metric(self):
        raw_val = 100.0
        priv_val = self.engine.privatize_telemetry_metric(raw_val, sensitivity=0.1)
        self.assertAlmostEqual(priv_val, raw_val, delta=10.0)

    def test_empty_clients_raises(self):
        with self.assertRaises(ValueError):
            self.engine.aggregate_gradients_federated([])

if __name__ == '__main__':
    unittest.main()
