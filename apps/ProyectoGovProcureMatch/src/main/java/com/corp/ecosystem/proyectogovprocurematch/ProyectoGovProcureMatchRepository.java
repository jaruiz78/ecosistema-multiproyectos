package com.corp.ecosystem.proyectogovprocurematch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoGovProcureMatchRepository extends JpaRepository<ProyectoGovProcureMatchEntity, UUID> {
    List<ProyectoGovProcureMatchEntity> findByTenantId(String tenantId);
}
