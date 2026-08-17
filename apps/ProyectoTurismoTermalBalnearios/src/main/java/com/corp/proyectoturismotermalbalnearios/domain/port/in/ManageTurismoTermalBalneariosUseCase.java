package com.corp.proyectoturismotermalbalnearios.domain.port.in;

import com.corp.proyectoturismotermalbalnearios.domain.model.TurismoTermalBalnearios;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageTurismoTermalBalneariosUseCase {
    TurismoTermalBalnearios createTurismoTermalBalnearios(String tenantId, String title, double value);
    Optional<TurismoTermalBalnearios> findTurismoTermalBalneariosById(String id, String tenantId);
    TurismoTermalBalnearios processOptimization(String id, String tenantId);
}
