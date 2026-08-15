package com.corp.ecosystem.proyectoplayasinteligentescostas;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoPlayasInteligentesCostasService {
    private final ProyectoPlayasInteligentesCostasRepository repository;
    public ProyectoPlayasInteligentesCostasService(ProyectoPlayasInteligentesCostasRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoPlayasInteligentesCostasEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoPlayasInteligentesCostasEntity entity = new ProyectoPlayasInteligentesCostasEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoPlayasInteligentesCostasEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
