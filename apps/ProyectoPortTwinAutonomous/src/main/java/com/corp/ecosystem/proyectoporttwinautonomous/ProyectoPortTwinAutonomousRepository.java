package com.corp.ecosystem.proyectoporttwinautonomous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoPortTwinAutonomousRepository extends JpaRepository<ProyectoPortTwinAutonomousEntity, UUID> {
    List<ProyectoPortTwinAutonomousEntity> findByTenantId(String tenantId);
}
