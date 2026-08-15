# Módulo 4 - Lección 4: Integración OSRM, Arquitectura Clean Flutter & Optimización Térmica

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es OSRM y por qué ahorramos batería en movilidad?
**OSRM (Open Source Routing Machine)** es un motor de cálculo de rutas ultrarrápido basado en datos de OpenStreetMap.

En aplicaciones de movilidad activas continuamente durante horas en el coche del conductor (`AppViajes`), consultar el GPS cada medio segundo calienta el procesador del teléfono y agota la batería en 40 minutos. Aplicamos **muestreo GPS adaptativo** para solicitar ubicaciones solo cuando sea estrictamente necesario según la velocidad actual del vehículo.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Capa de UI (Widgets)
        MAP[FlutterMap Widget]
    end

    subgraph Capa de Lógica de Negocio
        BLOC[RideNavigationBloc]
        BATTERY[AdaptiveGPSManager]
    end

    subgraph Capa de Datos / Infraestructura
        OSRM[OSRM Route Repository]
        GPS[Geolocator Stream]
    end

    MAP --> BLOC
    BLOC --> BATTERY
    BATTERY -->|Muestreo Adaptativo 2s/15s| GPS
    BLOC -->|Consulta Ruta| OSRM
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

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

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Muestreo Adaptativo GPS vs Estrés Térmico

| Velocidad Vehículo | Intervalo de Muestreo | Frecuencia CPU | Consumo Batería |
| :--- | :--- | :--- | :--- |
| **Parado / Semáforo (< 5 km/h)** | 15 segundos | Mínima | **~2% por hora** |
| **Tráfico Urbano (5-30 km/h)** | 5 segundos | Media | ~5% por hora |
| **Carretera (> 30 km/h)** | 2 segundos | Alta | ~9% por hora |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Mantener el listener GPS activo en segundo plano a máxima precisión constante**:
   * *Síntoma*: El sistema operativo (Android/iOS) fuerza el cierre de la app por degradación térmica o consumo abusivo de batería.
   * *Solución*: Implementa el gestor de geofencing adaptativo con degradación de frecuencia cuando la app no está enfocada.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Integración OSRM, Arquitectura Clean Flutter & Optimización Térmica** a un estudiante de secundaria, **sin usar las palabras:** "Integración", "OSRM,", "Arquitectura" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.

---

## ⚙️ Primeros Principios & Fundamentos Conceptuales
1. **Descomposición Atómica:** Cada componente en Módulo 4 - Lección 4: Integración OSRM, Arquitectura Clean Flutter & Optimización Térmica se modela de forma determinista y sin estado mutable compartido.
2. **Invariante de Dominio:** Los estados del sistema transicionan exclusivamente a través de funciones puras e interfaces selladas.
3. **Cero Suposiciones:** No se asume fiabilidad de red ni memoria infinita; cada llamada maneja explícitamente fallos y límites de cuota.

