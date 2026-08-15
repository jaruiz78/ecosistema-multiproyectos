package com.corp.ecosystem.proyectoagroenergyvpp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoAgroEnergyVPPService {
    private final ProyectoAgroEnergyVPPRepository repository;
    public ProyectoAgroEnergyVPPService(ProyectoAgroEnergyVPPRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoAgroEnergyVPPEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoAgroEnergyVPPEntity entity = new ProyectoAgroEnergyVPPEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoAgroEnergyVPPEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
