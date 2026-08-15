package com.corp.ecosystem.proyectoregenerativeexperience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoRegenerativeExperienceRepository extends JpaRepository<ProyectoRegenerativeExperienceEntity, UUID> {
    List<ProyectoRegenerativeExperienceEntity> findByTenantId(String tenantId);
}
