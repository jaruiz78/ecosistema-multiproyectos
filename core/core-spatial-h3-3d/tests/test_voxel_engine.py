import sys
import os
import pytest

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from voxel_engine import Voxel3DEngine

def test_voxel3d_encoding():
    engine = Voxel3DEngine(altitude_resolution_m=20.0)
    
    h3_index = 0x881f1d4887fffff
    altitude_m = 105.0 # Debe mapear a nivel int(105 // 20) = 5
    
    voxel_id = engine.encode_voxel3d(h3_index, altitude_m)
    
    # Extraer nivel de altitud y h3 index
    decoded_alt_level = voxel_id & 0xFFFF
    decoded_h3 = voxel_id >> 16
    
    assert decoded_alt_level == 5
    assert decoded_h3 == h3_index

def test_voxel3d_collision_detection():
    engine = Voxel3DEngine(altitude_resolution_m=25.0)
    
    h3_a = 0x881f1d4887fffff
    h3_b = 0x881f1d4887fffff
    h3_c = 0x881f1d4881fffff
    
    v1 = engine.encode_voxel3d(h3_a, 50.0)
    v2 = engine.encode_voxel3d(h3_b, 60.0) # Ambos caen en el nivel 2 (50//25 = 2, 60//25 = 2)
    v3 = engine.encode_voxel3d(h3_c, 50.0)
    
    assert engine.check_voxel_collision(v1, v2) is True # Misma celda y mismo nivel de altitud
    assert engine.check_voxel_collision(v1, v3) is False # Distinta celda H3
