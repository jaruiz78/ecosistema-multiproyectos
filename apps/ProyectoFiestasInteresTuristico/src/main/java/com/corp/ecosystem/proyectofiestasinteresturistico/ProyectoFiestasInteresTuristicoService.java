package com.corp.ecosystem.proyectofiestasinteresturistico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoFiestasInteresTuristicoService {
    private final ProyectoFiestasInteresTuristicoRepository repository;
    public ProyectoFiestasInteresTuristicoService(ProyectoFiestasInteresTuristicoRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoFiestasInteresTuristicoEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoFiestasInteresTuristicoEntity entity = new ProyectoFiestasInteresTuristicoEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoFiestasInteresTuristicoEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
