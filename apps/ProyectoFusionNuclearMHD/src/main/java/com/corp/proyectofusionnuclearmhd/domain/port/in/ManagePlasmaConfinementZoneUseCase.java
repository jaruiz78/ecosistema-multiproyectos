package com.corp.proyectofusionnuclearmhd.domain.port.in;

import com.corp.proyectofusionnuclearmhd.domain.model.PlasmaConfinementZone;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManagePlasmaConfinementZoneUseCase {
    PlasmaConfinementZone createPlasmaConfinementZone(String tenantId, String title, double value);
    Optional<PlasmaConfinementZone> findPlasmaConfinementZoneById(String id, String tenantId);
    PlasmaConfinementZone processOptimization(String id, String tenantId);
}
