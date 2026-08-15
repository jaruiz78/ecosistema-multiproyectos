package com.corp.ecosystem.proyectofleetcoldchain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoFleetColdChainRepository extends JpaRepository<ProyectoFleetColdChainEntity, UUID> {
    List<ProyectoFleetColdChainEntity> findByTenantId(String tenantId);
}
