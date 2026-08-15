package com.corp.ecosystem.proyectodiputacionturismorural;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoDiputacionTurismoRuralRepository extends JpaRepository<ProyectoDiputacionTurismoRuralEntity, UUID> {
    List<ProyectoDiputacionTurismoRuralEntity> findByTenantId(String tenantId);
}
