"""
voxel_engine.py
-------------------------------------------------------------------------
core-spatial-h3-3d: Motor de Vóxeles H3 Tridimensionales
Combina identificadores H3 uint64 con niveles de altitud uint16 en O(1).
-------------------------------------------------------------------------
"""
import numpy as np

class Voxel3DEngine:
    def __init__(self, altitude_resolution_m=20.0):
        self.alt_step = altitude_resolution_m

    def encode_voxel3d(self, h3_uint64: int, altitude_m: float) -> int:
        """
        Codifica un vóoxel 3D combinando H3 uint64 (bits 16-79) con nivel de altitud uint16 (bits 0-15).
        """
        alt_level = int(max(0, altitude_m) // self.alt_step) & 0xFFFF
        return (h3_uint64 << 16) | alt_level

    def check_voxel_collision(self, voxel_a: int, voxel_b: int) -> bool:
        """
        Comprobación de colisión binaria en O(1).
        """
        return voxel_a == voxel_b
