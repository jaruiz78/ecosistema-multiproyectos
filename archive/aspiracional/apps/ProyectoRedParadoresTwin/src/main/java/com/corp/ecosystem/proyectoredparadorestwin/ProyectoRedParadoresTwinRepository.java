package com.corp.ecosystem.proyectoredparadorestwin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoRedParadoresTwinRepository extends JpaRepository<ProyectoRedParadoresTwinEntity, UUID> {
    List<ProyectoRedParadoresTwinEntity> findByTenantId(String tenantId);
}
