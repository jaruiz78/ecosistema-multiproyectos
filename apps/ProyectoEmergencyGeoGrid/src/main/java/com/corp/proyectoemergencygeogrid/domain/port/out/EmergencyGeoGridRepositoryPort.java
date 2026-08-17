package com.corp.proyectoemergencygeogrid.domain.port.out;

import com.corp.proyectoemergencygeogrid.domain.model.EmergencyGeoGrid;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_geoespacial_h3_osrm_movilidad">FACULTAD_IX: Geoespacial H3, OSRM & Movilidad</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface EmergencyGeoGridRepositoryPort {
    EmergencyGeoGrid save(EmergencyGeoGrid entity);
    Optional<EmergencyGeoGrid> findById(String id, String tenantId);
}
