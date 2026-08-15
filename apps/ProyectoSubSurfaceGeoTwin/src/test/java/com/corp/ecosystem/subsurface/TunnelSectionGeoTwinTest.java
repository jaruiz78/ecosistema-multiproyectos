package com.corp.ecosystem.subsurface;

import com.corp.ecosystem.subsurface.application.SubSurfaceGeoService;
import com.corp.ecosystem.subsurface.domain.TunnelSectionGeoTwin;
import com.corp.ecosystem.subsurface.domain.port.TunnelTwinRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoSubSurfaceGeoTwin.
 */
class TunnelSectionGeoTwinTest {

    static class InMemoryTunnelTwinRepository implements TunnelTwinRepositoryPort {
        private final Map<TunnelSectionGeoTwin.TunnelSectionId, TunnelSectionGeoTwin> storage = new ConcurrentHashMap<>();

        @Override
        public TunnelSectionGeoTwin save(TunnelSectionGeoTwin section) {
            storage.put(section.id(), section);
            return section;
        }

        @Override
        public Optional<TunnelSectionGeoTwin> findById(TunnelSectionGeoTwin.TunnelSectionId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryTunnelTwinRepository repository = new InMemoryTunnelTwinRepository();
    private final SubSurfaceGeoService service = new SubSurfaceGeoService(repository);

    @Test
    @DisplayName("Debe registrar sección de túnel y reportar estado STABLE_NORMAL con lecturas nominales")
    void shouldRegisterTunnelSectionInStableState() {
        TunnelSectionGeoTwin section = service.registerTunnelSection(
                "adif-alta-velocidad",
                "Túnel de Pajares - Tramo Norte",
                14.25,
                25.0, // 25mm max convergencia
                450.0 // 450 kPa max presión
        );

        assertNotNull(section.id());
        assertEquals(TunnelSectionGeoTwin.StructuralHealthStatus.STABLE_NORMAL, section.healthStatus());

        TunnelSectionGeoTwin updated = service.ingestSensorTelemetry(section.id(), 5.2, 120.0, 250.0);
        assertEquals(TunnelSectionGeoTwin.StructuralHealthStatus.STABLE_NORMAL, updated.healthStatus());
    }

    @Test
    @DisplayName("Debe disparar CRITICAL_GEOTECHNICAL_ALERT si la convergencia excede el límite de seguridad")
    void shouldTriggerCriticalAlertOnExcessiveConvergence() {
        TunnelSectionGeoTwin section = service.registerTunnelSection(
                "metro-madrid",
                "Línea 7B - San Fernando",
                8.40,
                15.0,
                300.0
        );

        // Deformación crítica de 18.5 mm (> 15.0 mm)
        TunnelSectionGeoTwin updated = service.ingestSensorTelemetry(section.id(), 18.5, 320.0, 1800.0);
        assertEquals(TunnelSectionGeoTwin.StructuralHealthStatus.CRITICAL_GEOTECHNICAL_ALERT, updated.healthStatus());
    }
}
