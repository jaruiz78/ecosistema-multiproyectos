package com.corp.ecosystem.proyectosalud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSaludRepository extends JpaRepository<ProyectoSaludEntity, UUID> {
    List<ProyectoSaludEntity> findByTenantId(String tenantId);
}
