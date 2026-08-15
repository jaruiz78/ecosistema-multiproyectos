package com.corp.ecosystem.proyectosyntheticbiologyfoundry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSyntheticBiologyFoundryRepository extends JpaRepository<ProyectoSyntheticBiologyFoundryEntity, UUID> {
    List<ProyectoSyntheticBiologyFoundryEntity> findByTenantId(String tenantId);
}
