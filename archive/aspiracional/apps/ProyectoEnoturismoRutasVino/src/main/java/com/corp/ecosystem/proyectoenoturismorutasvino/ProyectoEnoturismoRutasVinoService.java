package com.corp.ecosystem.proyectoenoturismorutasvino;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoEnoturismoRutasVinoService {
    private final ProyectoEnoturismoRutasVinoRepository repository;
    public ProyectoEnoturismoRutasVinoService(ProyectoEnoturismoRutasVinoRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoEnoturismoRutasVinoEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoEnoturismoRutasVinoEntity entity = new ProyectoEnoturismoRutasVinoEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoEnoturismoRutasVinoEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
