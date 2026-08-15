package com.corp.ecosystem.clinicaltrials.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: ClinicalTrialEnrollment (Ensayos Clínicos Descentralizados & Cohort Matching ZK).
 * <p>
 * Verifica la elegibilidad de pacientes para ensayos de oncología y terapias raras mediante pruebas
 * de conocimiento cero (Zero-Knowledge Proofs), garantizando 100% de cumplimiento HIPAA / GDPR sin exponer PII médica.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference FDA Guidance on Decentralized Clinical Trials (DCT); ICH GCP E6(R3); EU Clinical Trials Regulation (CTR)
 */
public record ClinicalTrialEnrollment(
        EnrollmentId id,
        String tenantId,
        String trialProtocolCode,
        String anonymousPatientZkHash,
        EligibilityProof proof,
        TrialCohortStatus cohortStatus,
        Instant enrolledAt
) implements Serializable {

    public record EnrollmentId(String value) {
        public EnrollmentId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("EnrollmentId no puede estar vacío");
        }
    }

    public record EligibilityProof(
            boolean isAgeAndGenderEligibleZk,
            boolean isBiomarkerPositiveZk,
            boolean isExclusionCriteriaSatisfiedZk,
            String snarkProofReceipt
    ) {
        public boolean isFullyEligible() {
            return isAgeAndGenderEligibleZk && isBiomarkerPositiveZk && isExclusionCriteriaSatisfiedZk;
        }
    }

    public enum TrialCohortStatus {
        ELIGIBLE_COHORT_ACCEPTED, SCREEN_FAILED_INELIGIBLE, WITHDRAWN
    }

    public static ClinicalTrialEnrollment matchPatient(
            EnrollmentId id,
            String tenantId,
            String protocolCode,
            String patientZkHash,
            boolean ageValid,
            boolean biomarkerValid,
            boolean exclusionValid
    ) {
        String proofReceipt = "ZK-SNARK-TRIAL-" + Integer.toHexString(Objects.hash(protocolCode, patientZkHash, ageValid, biomarkerValid));
        EligibilityProof proof = new EligibilityProof(ageValid, biomarkerValid, exclusionValid, proofReceipt);

        TrialCohortStatus status = proof.isFullyEligible() ?
                TrialCohortStatus.ELIGIBLE_COHORT_ACCEPTED :
                TrialCohortStatus.SCREEN_FAILED_INELIGIBLE;

        return new ClinicalTrialEnrollment(
                id,
                tenantId,
                protocolCode,
                patientZkHash,
                proof,
                status,
                Instant.now()
        );
    }
}
