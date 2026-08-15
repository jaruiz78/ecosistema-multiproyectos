package com.corp.ecosystem.proyectologistica;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoLogisticaService {
    private final ProyectoLogisticaRepository repository;
    public ProyectoLogisticaService(ProyectoLogisticaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoLogisticaEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoLogisticaEntity entity = new ProyectoLogisticaEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoLogisticaEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
