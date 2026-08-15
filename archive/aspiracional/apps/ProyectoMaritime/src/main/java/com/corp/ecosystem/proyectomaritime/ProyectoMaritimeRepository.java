package com.corp.ecosystem.proyectomaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoMaritimeRepository extends JpaRepository<ProyectoMaritimeEntity, UUID> {
    List<ProyectoMaritimeEntity> findByTenantId(String tenantId);
}
