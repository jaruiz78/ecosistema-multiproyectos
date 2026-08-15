package com.corp.ecosystem.proyectodualairdefense;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoDualAirDefenseService {
    private final ProyectoDualAirDefenseRepository repository;
    public ProyectoDualAirDefenseService(ProyectoDualAirDefenseRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoDualAirDefenseEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoDualAirDefenseEntity entity = new ProyectoDualAirDefenseEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoDualAirDefenseEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
