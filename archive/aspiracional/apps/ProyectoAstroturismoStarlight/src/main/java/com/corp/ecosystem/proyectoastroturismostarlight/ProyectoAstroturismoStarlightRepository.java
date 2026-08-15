package com.corp.ecosystem.proyectoastroturismostarlight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoAstroturismoStarlightRepository extends JpaRepository<ProyectoAstroturismoStarlightEntity, UUID> {
    List<ProyectoAstroturismoStarlightEntity> findByTenantId(String tenantId);
}
