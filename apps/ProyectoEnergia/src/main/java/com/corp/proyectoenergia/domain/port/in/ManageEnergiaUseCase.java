package com.corp.proyectoenergia.domain.port.in;

import com.corp.proyectoenergia.domain.model.Energia;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageEnergiaUseCase {
    Energia createEnergia(String tenantId, String title, double value);
    Optional<Energia> findEnergiaById(String id, String tenantId);
    Energia processOptimization(String id, String tenantId);
}
