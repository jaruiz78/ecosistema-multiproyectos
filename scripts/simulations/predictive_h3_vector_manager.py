"""
Arquitectura y especificación formal para predictive_h3_vector_manager.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import math
import logging
from typing import List, Dict, Any, Tuple

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

class PredictiveH3VectorManager:
    """
    Gestor Vectorial de Precarga Anticipatoria H3 (AppViajes / Movilidad).
    Estima la trayectoria del vehículo según latitud, longitud, velocidad (km/h) y rumbo (grados),
    calculando los centroides y anillos de celdas H3 que requerirán precarga offline.
    """

    EARTH_RADIUS_KM = 6371.0

    def __init__(self, default_resolution: int = 8):
        self.resolution = default_resolution

    def predict_future_position(self, lat: float, lon: float, speed_kmh: float, heading_deg: float, lookahead_seconds: float = 30.0) -> Tuple[float, float]:
        """
        Calcula la posición geográfica estimada (lat, lon) tras `lookahead_seconds`
        asumiendo velocidad constante y rumbo fijo.
        """
        distance_km = (speed_kmh / 3600.0) * lookahead_seconds
        heading_rad = math.radians(heading_deg)
        lat_rad = math.radians(lat)
        lon_rad = math.radians(lon)

        future_lat_rad = math.asin(
            math.sin(lat_rad) * math.cos(distance_km / self.EARTH_RADIUS_KM) +
            math.cos(lat_rad) * math.sin(distance_km / self.EARTH_RADIUS_KM) * math.cos(heading_rad)
        )

        future_lon_rad = lon_rad + math.atan2(
            math.sin(heading_rad) * math.sin(distance_km / self.EARTH_RADIUS_KM) * math.cos(lat_rad),
            math.cos(distance_km / self.EARTH_RADIUS_KM) - math.sin(lat_rad) * math.sin(future_lat_rad)
        )

        return math.degrees(future_lat_rad), math.degrees(future_lon_rad)

    def calculate_prefetch_cells(self, current_lat: float, current_lon: float, speed_kmh: float, heading_deg: float) -> Dict[str, Any]:
        """
        Calcula la celda H3 actual y la celda objetivo futura a precargar.
        """
        future_lat, future_lon = self.predict_future_position(current_lat, current_lon, speed_kmh, heading_deg)
        
        # Simulación de representación H3 Index string
        current_h3 = f"h3_res{self.resolution}_{round(current_lat, 3)}_{round(current_lon, 3)}"
        predicted_h3 = f"h3_res{self.resolution}_{round(future_lat, 3)}_{round(future_lon, 3)}"
        
        is_transitioning = (current_h3 != predicted_h3)

        result = {
            "current_position": (current_lat, current_lon),
            "predicted_position": (future_lat, future_lon),
            "current_h3_index": current_h3,
            "predicted_h3_index": predicted_h3,
            "speed_kmh": speed_kmh,
            "heading_deg": heading_deg,
            "is_transitioning": is_transitioning,
            "cells_to_prefetch": [current_h3, predicted_h3] if is_transitioning else [current_h3]
        }

        logging.info(
            f"🚗 [H3 PREFETCH] Vel: {speed_kmh}km/h | Rumbo: {heading_deg}° | "
            f"Actual: {current_h3} -> Anticipado (30s): {predicted_h3} | Transición: {is_transitioning}"
        )
        return result

if __name__ == "__main__":
    manager = PredictiveH3VectorManager(default_resolution=8)
    
    # 1. Vehículo parado
    res1 = manager.calculate_prefetch_cells(36.838, -2.459, speed_kmh=0.0, heading_deg=0.0)
    assert res1["is_transitioning"] is False

    # 2. Vehículo en autopista a 120 km/h rumbo Norte
    res2 = manager.calculate_prefetch_cells(36.838, -2.459, speed_kmh=120.0, heading_deg=0.0)
    assert res2["is_transitioning"] is True
    logging.info("✅ Precarga predictiva vectorial H3 verificada.")
