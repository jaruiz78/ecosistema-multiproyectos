package com.corp.ecosystem.proyectodefensa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ProyectoDefensaService {
    private final ProyectoDefensaRepository repository;
    public ProyectoDefensaService(ProyectoDefensaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoDefensaEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoDefensaEntity entity = new ProyectoDefensaEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoDefensaEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
