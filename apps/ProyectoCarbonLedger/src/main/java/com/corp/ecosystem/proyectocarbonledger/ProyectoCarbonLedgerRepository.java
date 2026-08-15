package com.corp.ecosystem.proyectocarbonledger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoCarbonLedgerRepository extends JpaRepository<ProyectoCarbonLedgerEntity, UUID> {
    List<ProyectoCarbonLedgerEntity> findByTenantId(String tenantId);
}
