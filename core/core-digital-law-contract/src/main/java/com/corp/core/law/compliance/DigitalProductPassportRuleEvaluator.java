package com.corp.core.law.compliance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Validador formal de directiva de Pasaporte Digital de Producto (EU Ecodesign / DPP Regulation).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record DigitalProductPassportRuleEvaluator() implements Serializable {

    public record DppPayload(
            String batchIdentifier,
            double carbonFootprintKgCo2e,
            double recycledContentPct,
            boolean hasRepairabilityManual,
            boolean cryptographicSealValid
    ) implements Serializable {}

    public record DppAuditResult(
            boolean approved,
            List<String> violations
    ) implements Serializable {}

    public static DppAuditResult evaluateDpp(DppPayload payload) {
        List<String> violations = new ArrayList<>();

        if (payload.carbonFootprintKgCo2e() < 0) {
            violations.add("Huella de carbono negativa no permitida sin certificación de sumidero adicional");
        }
        if (payload.recycledContentPct() < 0.0 || payload.recycledContentPct() > 100.0) {
            violations.add("Porcentaje de contenido reciclado fuera de rango [0, 100]");
        }
        if (!payload.hasRepairabilityManual()) {
            violations.add("Obligación de manual de reparabilidad no cumplida");
        }
        if (!payload.cryptographicSealValid()) {
            violations.add("Sello criptográfico del pasaporte inválido o alterado");
        }

        return new DppAuditResult(violations.isEmpty(), violations);
    }
}
