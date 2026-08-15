package com.corp.ecosystem.proyectoenergia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoEnergiaRepository extends JpaRepository<ProyectoEnergiaEntity, UUID> {
    List<ProyectoEnergiaEntity> findByTenantId(String tenantId);
}
