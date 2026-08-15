package com.corp.ecosystem.proyectotokenrwa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoTokenRWARepository extends JpaRepository<ProyectoTokenRWAEntity, UUID> {
    List<ProyectoTokenRWAEntity> findByTenantId(String tenantId);
}
