package com.corp.ecosystem.proyectoindustrialmicrogridmpc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoIndustrialMicrogridMPCService {
    private final ProyectoIndustrialMicrogridMPCRepository repository;
    public ProyectoIndustrialMicrogridMPCService(ProyectoIndustrialMicrogridMPCRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoIndustrialMicrogridMPCEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoIndustrialMicrogridMPCEntity entity = new ProyectoIndustrialMicrogridMPCEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoIndustrialMicrogridMPCEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
