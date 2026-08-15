package com.corp.ecosystem.proyectotaxcomplianceledger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoTaxComplianceLedgerRepository extends JpaRepository<ProyectoTaxComplianceLedgerEntity, UUID> {
    List<ProyectoTaxComplianceLedgerEntity> findByTenantId(String tenantId);
}
