package com.corp.ecosystem.proyectodefensa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoDefensaRepository extends JpaRepository<ProyectoDefensaEntity, UUID> {
    List<ProyectoDefensaEntity> findByTenantId(String tenantId);
}
