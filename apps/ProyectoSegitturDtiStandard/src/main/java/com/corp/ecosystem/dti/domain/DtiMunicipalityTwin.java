package com.corp.ecosystem.dti.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: DtiMunicipalityTwin (Destino Turístico Inteligente / Norma UNE 178501-178504).
 * Evalúa los 5 ejes Segittur: Gobernanza, Sostenibilidad, Accesibilidad, Innovación y Tecnología.
 */
public record DtiMunicipalityTwin(
        MunicipalityId id,
        String tenantId,
        String municipalityName,
        String autonomousCommunity,
        DtiAxesScores axesScores,
        SegitturDistinctionLevel distinctionLevel,
        Instant lastAuditedAt
) implements Serializable {

    public record MunicipalityId(String value) {
        public MunicipalityId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("MunicipalityId no puede estar vacío");
        }
    }

    public record DtiAxesScores(
            double governancePct,
            double sustainabilityPct,
            double accessibilityPct,
            double innovationPct,
            double technologyPct
    ) {
        public double overallAveragePct() {
            return (governancePct + sustainabilityPct + accessibilityPct + innovationPct + technologyPct) / 5.0;
        }
    }

    public enum SegitturDistinctionLevel {
        DTI_CERTIFIED_EXCELLENCE, DTI_ADHERED_IN_PROGRESS, NON_COMPLIANT
    }

    public static DtiMunicipalityTwin auditDestination(
            MunicipalityId id,
            String tenantId,
            String name,
            String community,
            DtiAxesScores scores
    ) {
        double avg = scores.overallAveragePct();
        SegitturDistinctionLevel level = (avg >= 80.0 && scores.accessibilityPct() >= 75.0) ?
                SegitturDistinctionLevel.DTI_CERTIFIED_EXCELLENCE :
                (avg >= 50.0 ? SegitturDistinctionLevel.DTI_ADHERED_IN_PROGRESS : SegitturDistinctionLevel.NON_COMPLIANT);

        return new DtiMunicipalityTwin(
                id,
                tenantId,
                name,
                community,
                scores,
                level,
                Instant.now()
        );
    }
}
