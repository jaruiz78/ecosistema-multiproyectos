package com.corp.proyectocirculartextiledpp.domain.port.in;

import com.corp.proyectocirculartextiledpp.domain.model.CircularTextileDPP;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageCircularTextileDPPUseCase {
    CircularTextileDPP createCircularTextileDPP(String tenantId, String title, double value);
    Optional<CircularTextileDPP> findCircularTextileDPPById(String id, String tenantId);
    CircularTextileDPP processOptimization(String id, String tenantId);
}
