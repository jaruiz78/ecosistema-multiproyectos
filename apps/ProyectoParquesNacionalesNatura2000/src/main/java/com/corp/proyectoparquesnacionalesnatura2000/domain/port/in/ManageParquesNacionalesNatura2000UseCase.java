package com.corp.proyectoparquesnacionalesnatura2000.domain.port.in;

import com.corp.proyectoparquesnacionalesnatura2000.domain.model.ParquesNacionalesNatura2000;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageParquesNacionalesNatura2000UseCase {
    ParquesNacionalesNatura2000 createParquesNacionalesNatura2000(String tenantId, String title, double value);
    Optional<ParquesNacionalesNatura2000> findParquesNacionalesNatura2000ById(String id, String tenantId);
    ParquesNacionalesNatura2000 processOptimization(String id, String tenantId);
}
