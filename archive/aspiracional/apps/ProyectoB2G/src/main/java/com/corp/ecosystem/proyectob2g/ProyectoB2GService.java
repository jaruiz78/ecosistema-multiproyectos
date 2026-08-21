package com.corp.ecosystem.proyectob2g;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ProyectoB2GService {
    private final ProyectoB2GRepository repository;
    public ProyectoB2GService(ProyectoB2GRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoB2GEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoB2GEntity entity = new ProyectoB2GEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoB2GEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
