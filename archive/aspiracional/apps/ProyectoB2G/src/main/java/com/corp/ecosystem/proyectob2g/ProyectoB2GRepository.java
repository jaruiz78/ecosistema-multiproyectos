package com.corp.ecosystem.proyectob2g;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoB2GRepository extends JpaRepository<ProyectoB2GEntity, UUID> {
    List<ProyectoB2GEntity> findByTenantId(String tenantId);
}
