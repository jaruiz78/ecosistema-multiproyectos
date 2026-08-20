# ADR-046: Ingesta Enriquecida de Telemetría TaxiCaller, Cinemática Dinámica y Visualización 360° en Radar y Detalle Web

## Estado
Aceptado e Implementado en local (Java 25 LTS, Spring Boot 4, React 19) con 309 tests unitarios/arquitectura en verde y 21 tests de interfaz de usuario superados.

## Contexto y Problema
En las versiones iniciales de la integración con TaxiCaller (`/api/v1/booker/order/{order_id}/track`), el sistema extraía únicamente coordenadas bidimensionales discretas (`[lon, lat]`) y marca de tiempo (`timestamp`), descartando el resto de las variables cinemáticas, de navegación, económicas y de estado del terminal móvil provistas por la API.

Esta pérdida de información limitaba la capacidad operativa en el Centro de Control:
1. **Ausencia de Orientación Vehicular**: Los marcadores del radar táctico no mostraban el rumbo (`bearing`), impidiendo distinguir la dirección de avance en glorietas y autovías.
2. **Incertidumbre de Precisión GPS**: No se filtraban ubicaciones imprecisas (`accuracy > 50m` o `hdop` elevado) ni se exponía la dispersión del receptor GPS.
3. **Falta de Estimación Dinámica de Tráfico**: Los operadores no disponían del tiempo restante de llegada (`remaining_duration`/`eta`) ni la distancia restante real (`remaining_distance`) calculada por el motor de tráfico.
4. **Opacidad Económica y de Tiempos**: Los suplementos (`extras`), lecturas en vivo del taxímetro (`meter_fare`) y tiempos de espera (`waiting_time_seconds`) no se persistían ni mostraban.
5. **Carencia de Diagnóstico IoT**: Se desconocía el nivel de batería (`battery_level`) y cobertura celular (`signal_strength`) del smartphone del conductor en campo.

## Decisiones de Arquitectura

1. **Ingesta Integral en Dominio DDD Puro (Java 25 Records)**:
   - Se extendieron los records inmutables `TcTrackResponseDto`, `TrackingSnapshot` y `JobEntity.LastLocation` con los 13 campos telemétricos adicionales sin introducir dependencias de infraestructura ni anotaciones reflexivas ruidosas.
   - Se preservó compatibilidad retrospectiva mediante constructores sobrecargados con valores por defecto seguros.
   - Se implementó `TrackingSnapshot.hasValidPrecision(maxAccuracy)` para validación determinista de calidad GPS.

2. **Persistencia Tipada y Segura en Firestore**:
   - `FirestoreJobRepositoryAdapter` serializa y deserializa de forma robusta los mapas de telemetría, asignando tipos `Number` y `String` limpios para evitar incoherencias de tipos NoSQL.

3. **Exposición BFF para el Frontend**:
   - `TrackingResponse` y `BookingController` exponen la telemetría en el endpoint unificado `/api/v1/frontend/bookings/live-tracking` y `/api/v1/frontend/tracking/jobs/{hbxReference}`.

4. **Procesamiento Concurrente en Frontend sin Bloqueo de UI**:
   - `gps_telemetry.worker.ts` recibe e interpola el rumbo (`bearing`) y métricas telemétricas en un hilo Web Worker dedicado.
   - `LiveRadarCanvas.tsx` renderiza las flechas de orientación a 60fps sobre HTML5 Canvas mediante transformaciones afines optimizadas.
   - `RadarView.tsx` aplica rotación CSS dinámica en marcadores Leaflet y decodifica `route_polyline` en $O(N)$ sin bibliotecas externas.

5. **Panel 360° en Detalle de Reserva (`BookingDetailModal.tsx`)**:
   - Nueva tarjeta de visualización dividida en 4 cuadrantes: Cinemática (Velocidad/Rumbo/Precisión/Altitud), Tráfico (ETA/Distancia), IoT (Batería/Señal) y Economía (Taxímetro/Extras/Espera).
   - Botón interactivo de actualización bajo demanda.

## Consecuencias y Verificación
- **Fidelidad Telemétrica Completa**: Disponibilidad en tiempo real de todas las métricas de conducción y hardware.
- **Rendimiento AOT y Loom**: Procesamiento en memoria en $O(1)$ sin Carrier Thread Pinning.
- **Validación Automatizada**:
  - 309 tests unitarios y de arquitectura en `backend-java` aprobados al 100%.
  - 21 tests unitarios en `frontend` con Vitest y compilación Vite aprobada.
