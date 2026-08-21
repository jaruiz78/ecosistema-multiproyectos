package com.corp.proyectonuclearfusionstellarator.application.port.out;

import com.corp.proyectonuclearfusionstellarator.domain.StellaratorMagneticField;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface StellaratorRepositoryPort {
    void save(StellaratorMagneticField field);
    Optional<StellaratorMagneticField> findById(String reactorId);
}
