package com.corp.ecosystem.proyectopharmacoldchain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoPharmaColdChainRepository extends JpaRepository<ProyectoPharmaColdChainEntity, UUID> {
    List<ProyectoPharmaColdChainEntity> findByTenantId(String tenantId);
}
