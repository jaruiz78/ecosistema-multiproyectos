package com.corp.ecosystem.proyectocircular;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoCircularRepository extends JpaRepository<ProyectoCircularEntity, UUID> {
    List<ProyectoCircularEntity> findByTenantId(String tenantId);
}
