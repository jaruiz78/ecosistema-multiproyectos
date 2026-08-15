package com.corp.ecosystem.proyectoemergencygeogrid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoEmergencyGeoGridRepository extends JpaRepository<ProyectoEmergencyGeoGridEntity, UUID> {
    List<ProyectoEmergencyGeoGridEntity> findByTenantId(String tenantId);
}
