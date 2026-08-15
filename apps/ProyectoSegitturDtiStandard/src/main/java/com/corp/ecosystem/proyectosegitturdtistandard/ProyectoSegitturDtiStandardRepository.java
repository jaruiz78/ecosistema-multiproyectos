package com.corp.ecosystem.proyectosegitturdtistandard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSegitturDtiStandardRepository extends JpaRepository<ProyectoSegitturDtiStandardEntity, UUID> {
    List<ProyectoSegitturDtiStandardEntity> findByTenantId(String tenantId);
}
