## 2026-08-09T18:40:42Z
Apply the fix strategy identified by Explorer M4 Iteration 3 to AppViajes/services/backend-api:

1. Fix AsyncAiIntegrationTest / GpsPoint class issue:
   - Create top-level record file: /home/jaruiz/Desarrollo/AppViajes/services/backend-api/src/main/java/ai/itinera/backend/application/service/GpsPoint.java (package ai.itinera.backend.application.service; public record GpsPoint(double latitude, double longitude, double altitude, String timestamp) {} or matching fields).
   - Remove inner record declaration from UgcVideoService.java.
   - Update imports / usages in ItineraryController.java and UgcVideoServiceTest.java as needed.

2. Fix TelemetryGzipIntegrationTest missing bean issue:
   - Update TelemetryGzipIntegrationTest.java to ensure @Import({TelemetryController.class, GzipDecompressionFilter.class}) is present and that @MockitoBean (or @MockBean) mocks all 4 required dependencies (RescueModeService, PubSubPublisherAdapter, TelemetryDlqService, Storage).

3. Run verification:
   - Execute `mvn clean test` in /home/jaruiz/Desarrollo/AppViajes/services/backend-api.
   - Verify 100% green test execution.
