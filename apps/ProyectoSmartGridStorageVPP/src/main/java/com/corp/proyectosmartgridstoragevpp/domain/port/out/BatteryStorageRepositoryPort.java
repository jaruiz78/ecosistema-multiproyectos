package com.corp.proyectosmartgridstoragevpp.domain.port.out;

import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface BatteryStorageRepositoryPort {
    BatteryStorageUnit save(BatteryStorageUnit unit);
    Optional<BatteryStorageUnit> findById(String batteryId);
}
