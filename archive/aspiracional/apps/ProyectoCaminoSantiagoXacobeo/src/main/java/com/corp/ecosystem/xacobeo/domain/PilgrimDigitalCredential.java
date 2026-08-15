package com.corp.ecosystem.xacobeo.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: PilgrimDigitalCredential (Camino de Santiago / Credencial Digital Xacobeo).
 */
public record PilgrimDigitalCredential(
        CredentialId id,
        String tenantId,
        String anonymousPilgrimHash,
        String routeName,
        List<StageStamp> stamps,
        CompostelaStatus compostelaStatus,
        Instant issuedAt
) implements Serializable {

    public record CredentialId(String value) {
        public CredentialId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("CredentialId no puede estar vacío");
        }
    }

    public record StageStamp(
            String localityName,
            long h3IndexRes8,
            double kmAccumulated,
            long timestampEpochMs,
            String shelterZkProof
    ) {}

    public enum CompostelaStatus {
        ELIGIBLE_100KM_COMPLETED, IN_PROGRESS_ON_WAY, REQUISITE_NOT_MET
    }

    public static PilgrimDigitalCredential issue(CredentialId id, String tenantId, String pilgrimHash, String route) {
        return new PilgrimDigitalCredential(id, tenantId, pilgrimHash, route, List.of(), CompostelaStatus.IN_PROGRESS_ON_WAY, Instant.now());
    }

    public PilgrimDigitalCredential addStamp(StageStamp stamp) {
        var updatedStamps = new java.util.ArrayList<>(this.stamps);
        updatedStamps.add(stamp);

        double totalKm = updatedStamps.stream().mapToDouble(StageStamp::kmAccumulated).max().orElse(0.0);
        CompostelaStatus status = (totalKm >= 100.0 && updatedStamps.size() >= 2) ?
                CompostelaStatus.ELIGIBLE_100KM_COMPLETED :
                CompostelaStatus.IN_PROGRESS_ON_WAY;

        return new PilgrimDigitalCredential(this.id, this.tenantId, this.anonymousPilgrimHash, this.routeName, List.copyOf(updatedStamps), status, this.issuedAt);
    }
}
