package com.corp.ecosystem.proyectov2g;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoV2GRepository extends JpaRepository<ProyectoV2GEntity, UUID> {
    List<ProyectoV2GEntity> findByTenantId(String tenantId);
}
