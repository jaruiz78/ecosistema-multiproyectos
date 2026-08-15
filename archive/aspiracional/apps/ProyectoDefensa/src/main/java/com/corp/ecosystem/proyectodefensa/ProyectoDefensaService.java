package com.corp.ecosystem.proyectodefensa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoDefensaService {
    private final ProyectoDefensaRepository repository;
    public ProyectoDefensaService(ProyectoDefensaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoDefensaEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoDefensaEntity entity = new ProyectoDefensaEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoDefensaEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
