package com.corp.ecosystem.proyectosmartstreetlightingv2g;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSmartStreetLightingV2GRepository extends JpaRepository<ProyectoSmartStreetLightingV2GEntity, UUID> {
    List<ProyectoSmartStreetLightingV2GEntity> findByTenantId(String tenantId);
}
