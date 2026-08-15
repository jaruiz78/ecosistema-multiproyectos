package com.corp.ecosystem.proyectoquantumsatellitesync;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoQuantumSatelliteSyncRepository extends JpaRepository<ProyectoQuantumSatelliteSyncEntity, UUID> {
    List<ProyectoQuantumSatelliteSyncEntity> findByTenantId(String tenantId);
}
