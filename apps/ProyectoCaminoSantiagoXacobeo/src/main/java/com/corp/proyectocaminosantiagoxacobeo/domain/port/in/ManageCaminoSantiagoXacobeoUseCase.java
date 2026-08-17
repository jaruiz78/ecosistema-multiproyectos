package com.corp.proyectocaminosantiagoxacobeo.domain.port.in;

import com.corp.proyectocaminosantiagoxacobeo.domain.model.CaminoSantiagoXacobeo;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageCaminoSantiagoXacobeoUseCase {
    CaminoSantiagoXacobeo createCaminoSantiagoXacobeo(String tenantId, String title, double value);
    Optional<CaminoSantiagoXacobeo> findCaminoSantiagoXacobeoById(String id, String tenantId);
    CaminoSantiagoXacobeo processOptimization(String id, String tenantId);
}
