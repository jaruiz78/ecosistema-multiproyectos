package com.corp.proyectoagroenergyvpp.domain.port.in;

import com.corp.proyectoagroenergyvpp.domain.model.AgroEnergyVPP;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageAgroEnergyVPPUseCase {
    AgroEnergyVPP createAgroEnergyVPP(String tenantId, String title, double value);
    Optional<AgroEnergyVPP> findAgroEnergyVPPById(String id, String tenantId);
    AgroEnergyVPP processOptimization(String id, String tenantId);
}
