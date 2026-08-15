package com.corp.ecosystem.proyectoagrobiorobotics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoAgroBioRoboticsService {
    private final ProyectoAgroBioRoboticsRepository repository;
    public ProyectoAgroBioRoboticsService(ProyectoAgroBioRoboticsRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoAgroBioRoboticsEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoAgroBioRoboticsEntity entity = new ProyectoAgroBioRoboticsEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoAgroBioRoboticsEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
