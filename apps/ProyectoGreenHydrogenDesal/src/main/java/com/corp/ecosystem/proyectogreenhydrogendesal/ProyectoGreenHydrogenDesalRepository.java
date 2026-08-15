package com.corp.ecosystem.proyectogreenhydrogendesal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoGreenHydrogenDesalRepository extends JpaRepository<ProyectoGreenHydrogenDesalEntity, UUID> {
    List<ProyectoGreenHydrogenDesalEntity> findByTenantId(String tenantId);
}
