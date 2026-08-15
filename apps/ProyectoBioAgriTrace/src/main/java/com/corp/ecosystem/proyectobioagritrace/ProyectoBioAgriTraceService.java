package com.corp.ecosystem.proyectobioagritrace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoBioAgriTraceService {
    private final ProyectoBioAgriTraceRepository repository;
    public ProyectoBioAgriTraceService(ProyectoBioAgriTraceRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoBioAgriTraceEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoBioAgriTraceEntity entity = new ProyectoBioAgriTraceEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoBioAgriTraceEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
