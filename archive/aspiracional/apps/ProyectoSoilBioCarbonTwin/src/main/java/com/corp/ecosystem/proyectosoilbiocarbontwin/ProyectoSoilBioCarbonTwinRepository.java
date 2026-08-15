package com.corp.ecosystem.proyectosoilbiocarbontwin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSoilBioCarbonTwinRepository extends JpaRepository<ProyectoSoilBioCarbonTwinEntity, UUID> {
    List<ProyectoSoilBioCarbonTwinEntity> findByTenantId(String tenantId);
}
