package com.corp.ecosystem.proyectodroneairspaceuspace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoDroneAirspaceUSpaceRepository extends JpaRepository<ProyectoDroneAirspaceUSpaceEntity, UUID> {
    List<ProyectoDroneAirspaceUSpaceEntity> findByTenantId(String tenantId);
}
