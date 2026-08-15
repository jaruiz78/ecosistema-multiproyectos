package com.corp.ecosystem.proyectogeneralista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoGeneralistaRepository extends JpaRepository<ProyectoGeneralistaEntity, UUID> {
    List<ProyectoGeneralistaEntity> findByTenantId(String tenantId);
}
