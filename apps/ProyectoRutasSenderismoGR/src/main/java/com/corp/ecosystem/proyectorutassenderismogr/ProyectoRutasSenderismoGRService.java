package com.corp.ecosystem.proyectorutassenderismogr;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoRutasSenderismoGRService {
    private final ProyectoRutasSenderismoGRRepository repository;
    public ProyectoRutasSenderismoGRService(ProyectoRutasSenderismoGRRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoRutasSenderismoGREntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoRutasSenderismoGREntity entity = new ProyectoRutasSenderismoGREntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoRutasSenderismoGREntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
