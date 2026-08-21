package com.corp.proyectocarbondirectaircapture.domain.port.out;

import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface DacFacilityRepositoryPort {
    DirectAirCaptureFacility save(DirectAirCaptureFacility facility);
    Optional<DirectAirCaptureFacility> findById(String facilityId);
}
