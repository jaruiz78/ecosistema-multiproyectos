# Módulo 4 - Lección 2: Integración OSRM, Arquitectura Clean Flutter & Optimización Térmica

## 1. Arquitectura de Navegación Móvil (AppViajes)

La arquitectura de la aplicación Flutter móvil se estructura en 3 capas bien delimitadas (UI, Logic, Data) para asegurar capacidad Offline-First y protección de la batería del terminal.

```mermaid
graph TD
    subgraph Capa de Interfaz de Usuario / UI (Flutter Widgets)
        MAP[FlutterMap / Mapbox Widget]
        PANEL[RideRequestPanel Widget]
    end

    subgraph Capa de Lógica de Negocio / State Management
        BLOC[RideNavigationBloc / State]
        BATTERY[AdaptiveGPSManager]
    end

    subgraph Capa de Datos & Adaptadores / Data Layer
        OSRM[OSRM Route Repository]
        CACHE[Hive Local Storage]
        SENSOR[Geolocator GPS Stream]
    end

    MAP -->|User Event| BLOC
    PANEL -->|Action| BLOC
    BLOC -->|Adjust Frequency| BATTERY
    BATTERY -->|Filtered Location| SENSOR
    BLOC -->|Fetch Route| OSRM
    OSRM -->|Backup Route| CACHE
```

---

## 2. Muestreo GPS Adaptativo (Protección Térmica & Batería)

Para evitar el sobrecalentamiento del teléfono del conductor durante jornadas prolongadas de navegación:

* **En movimiento rápido (>30 km/h)**: Frecuencia de muestreo GPS alta (cada 2 segundos).
* **En vehículo parado / Semáforo (<5 km/h)**: Frecuencia de muestreo baja (cada 15 segundos).
* **App en segundo plano**: Transición automática a bajo consumo (Geofencing).

```dart
// Snippet Dart: Muestreo adaptativo de GPS
import 'dart:async';

enum BatteryStateMode { highAccuracy, powerSave }

class AdaptiveGPSManager {
  BatteryStateMode _currentMode = BatteryStateMode.highAccuracy;
  
  Duration calculateSamplingInterval(double currentSpeedKmh) {
    if (currentSpeedKmh < 5.0) {
      _currentMode = BatteryStateMode.powerSave;
      return const Duration(seconds: 15); // Ahorro de batería
    } else {
      _currentMode = BatteryStateMode.highAccuracy;
      return const Duration(seconds: 2); // Alta precisión en ruta
    }
  }
}
```

---

## 3. Integración con OSRM (Open Source Routing Machine)

El cliente HTTP en Dart consulta la API OSRM para obtener polilíneas decodificadas y duraciones estimadas de trayecto (ETA) en milisegundos.

```dart
import 'http' as http;
import 'convert' as json;

class OSRMRoutingRepository {
  final String osrmBaseUrl;

  OSRMRoutingRepository({required this.osrmBaseUrl});

  Future<Map<String, dynamic>> fetchRoute(double startLat, double startLng, double endLat, double endLng) async {
    final url = Uri.parse('$osrmBaseUrl/route/v1/driving/$startLng,$startLat;$endLng,$endLat?overview=full&geometries=geojson');
    final response = await http.get(url);

    if (response.statusCode == 200) {
      final data = json.jsonDecode(response.body);
      final route = data['routes'][0];
      return {
        'distance_meters': route['distance'],
        'duration_seconds': route['duration'],
        'geometry': route['geometry'],
      };
    } else {
      throw Exception('Failed to fetch route from OSRM');
    }
  }
}
```
