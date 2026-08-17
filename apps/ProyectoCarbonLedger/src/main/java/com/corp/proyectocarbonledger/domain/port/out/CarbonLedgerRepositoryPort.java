package com.corp.proyectocarbonledger.domain.port.out;

import com.corp.proyectocarbonledger.domain.model.CarbonLedger;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_sistemas_distribuidos">FACULTAD_II: Sistemas Distribuidos, Consenso & TLA+</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface CarbonLedgerRepositoryPort {
    CarbonLedger save(CarbonLedger entity);
    Optional<CarbonLedger> findById(String id, String tenantId);
}
