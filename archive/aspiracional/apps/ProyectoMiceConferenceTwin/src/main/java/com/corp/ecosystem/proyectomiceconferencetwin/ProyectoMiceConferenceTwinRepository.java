package com.corp.ecosystem.proyectomiceconferencetwin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoMiceConferenceTwinRepository extends JpaRepository<ProyectoMiceConferenceTwinEntity, UUID> {
    List<ProyectoMiceConferenceTwinEntity> findByTenantId(String tenantId);
}
