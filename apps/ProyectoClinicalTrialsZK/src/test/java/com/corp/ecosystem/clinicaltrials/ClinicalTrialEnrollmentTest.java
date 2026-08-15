package com.corp.ecosystem.clinicaltrials;

import com.corp.ecosystem.clinicaltrials.application.ClinicalTrialsZkService;
import com.corp.ecosystem.clinicaltrials.domain.ClinicalTrialEnrollment;
import com.corp.ecosystem.clinicaltrials.domain.port.ClinicalTrialRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoClinicalTrialsZK.
 */
class ClinicalTrialEnrollmentTest {

    static class InMemoryClinicalTrialRepository implements ClinicalTrialRepositoryPort {
        private final Map<ClinicalTrialEnrollment.EnrollmentId, ClinicalTrialEnrollment> storage = new ConcurrentHashMap<>();

        @Override
        public ClinicalTrialEnrollment save(ClinicalTrialEnrollment enrollment) {
            storage.put(enrollment.id(), enrollment);
            return enrollment;
        }

        @Override
        public Optional<ClinicalTrialEnrollment> findById(ClinicalTrialEnrollment.EnrollmentId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryClinicalTrialRepository repository = new InMemoryClinicalTrialRepository();
    private final ClinicalTrialsZkService service = new ClinicalTrialsZkService(repository);

    @Test
    @DisplayName("Debe aceptar paciente en cohorte con pruebas ZK válidas y cero exposición PII")
    void shouldAcceptEligiblePatientIntoCohortWithZkProof() {
        ClinicalTrialEnrollment enrollment = service.enrollPatientWithZkProof(
                "astrazeneca-oncology",
                "NCT04892019",
                "0x7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1fa3d677284addd200126d9069",
                true,  // Edad ZK válida
                true,  // Biomarcador EGFR+ ZK válido
                true   // Criterios de exclusión superados
        );

        assertNotNull(enrollment.id());
        assertEquals(ClinicalTrialEnrollment.TrialCohortStatus.ELIGIBLE_COHORT_ACCEPTED, enrollment.cohortStatus());
        assertTrue(enrollment.proof().isFullyEligible());
        assertNotNull(enrollment.proof().snarkProofReceipt());
    }

    @Test
    @DisplayName("Debe rechazar paciente en cribado si falta el biomarcador requerido")
    void shouldRejectPatientWhenBiomarkerCheckFails() {
        ClinicalTrialEnrollment enrollment = service.enrollPatientWithZkProof(
                "roche-immunology",
                "NCT05129988",
                "0x4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a",
                true,
                false, // Biomarcador ausente
                true
        );

        assertEquals(ClinicalTrialEnrollment.TrialCohortStatus.SCREEN_FAILED_INELIGIBLE, enrollment.cohortStatus());
        assertFalse(enrollment.proof().isFullyEligible());
    }
}
