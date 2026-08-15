package com.corp.ecosystem.proyectoecotasasoberanatax;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoEcotasaSoberanaTaxService {
    private final ProyectoEcotasaSoberanaTaxRepository repository;
    public ProyectoEcotasaSoberanaTaxService(ProyectoEcotasaSoberanaTaxRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoEcotasaSoberanaTaxEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoEcotasaSoberanaTaxEntity entity = new ProyectoEcotasaSoberanaTaxEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoEcotasaSoberanaTaxEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
