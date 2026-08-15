package com.corp.ecosystem.proyectoheritagedigitaltwin3d;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoHeritageDigitalTwin3DRepository extends JpaRepository<ProyectoHeritageDigitalTwin3DEntity, UUID> {
    List<ProyectoHeritageDigitalTwin3DEntity> findByTenantId(String tenantId);
}
