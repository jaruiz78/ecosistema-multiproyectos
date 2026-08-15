package com.corp.ecosystem.proyectotaxcomplianceledger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoTaxComplianceLedgerService {
    private final ProyectoTaxComplianceLedgerRepository repository;
    public ProyectoTaxComplianceLedgerService(ProyectoTaxComplianceLedgerRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoTaxComplianceLedgerEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoTaxComplianceLedgerEntity entity = new ProyectoTaxComplianceLedgerEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoTaxComplianceLedgerEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
