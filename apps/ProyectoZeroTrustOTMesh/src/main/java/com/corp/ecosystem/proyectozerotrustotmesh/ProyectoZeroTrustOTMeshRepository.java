package com.corp.ecosystem.proyectozerotrustotmesh;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoZeroTrustOTMeshRepository extends JpaRepository<ProyectoZeroTrustOTMeshEntity, UUID> {
    List<ProyectoZeroTrustOTMeshEntity> findByTenantId(String tenantId);
}
