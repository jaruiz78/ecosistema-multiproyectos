package com.corp.ecosystem.proyectoseamlessintermodalhub;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSeamlessIntermodalHubRepository extends JpaRepository<ProyectoSeamlessIntermodalHubEntity, UUID> {
    List<ProyectoSeamlessIntermodalHubEntity> findByTenantId(String tenantId);
}
