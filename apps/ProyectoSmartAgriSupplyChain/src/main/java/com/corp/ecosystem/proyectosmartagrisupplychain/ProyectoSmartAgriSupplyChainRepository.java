package com.corp.ecosystem.proyectosmartagrisupplychain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSmartAgriSupplyChainRepository extends JpaRepository<ProyectoSmartAgriSupplyChainEntity, UUID> {
    List<ProyectoSmartAgriSupplyChainEntity> findByTenantId(String tenantId);
}
