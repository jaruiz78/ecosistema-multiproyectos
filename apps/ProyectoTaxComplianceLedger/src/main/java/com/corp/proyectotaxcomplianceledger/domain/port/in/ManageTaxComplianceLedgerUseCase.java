package com.corp.proyectotaxcomplianceledger.domain.port.in;

import com.corp.proyectotaxcomplianceledger.domain.model.TaxComplianceLedger;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_sistemas_distribuidos">FACULTAD_II: Sistemas Distribuidos, Consenso & TLA+</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageTaxComplianceLedgerUseCase {
    TaxComplianceLedger createTaxComplianceLedger(String tenantId, String title, double value);
    Optional<TaxComplianceLedger> findTaxComplianceLedgerById(String id, String tenantId);
    TaxComplianceLedger processOptimization(String id, String tenantId);
}
