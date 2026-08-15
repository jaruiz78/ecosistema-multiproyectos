package com.corp.ecosystem.proyectosubsurfacegeotwin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSubSurfaceGeoTwinRepository extends JpaRepository<ProyectoSubSurfaceGeoTwinEntity, UUID> {
    List<ProyectoSubSurfaceGeoTwinEntity> findByTenantId(String tenantId);
}
