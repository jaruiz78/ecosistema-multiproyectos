package com.corp.ecosystem.proyectosmartwaterdesal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSmartWaterDesalRepository extends JpaRepository<ProyectoSmartWaterDesalEntity, UUID> {
    List<ProyectoSmartWaterDesalEntity> findByTenantId(String tenantId);
}
