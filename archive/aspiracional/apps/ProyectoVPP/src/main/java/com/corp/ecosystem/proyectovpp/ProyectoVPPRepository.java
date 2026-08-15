package com.corp.ecosystem.proyectovpp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoVPPRepository extends JpaRepository<ProyectoVPPEntity, UUID> {
    List<ProyectoVPPEntity> findByTenantId(String tenantId);
}
