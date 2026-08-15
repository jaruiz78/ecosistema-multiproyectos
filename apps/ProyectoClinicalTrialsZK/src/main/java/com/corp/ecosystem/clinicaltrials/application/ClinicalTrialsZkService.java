package com.corp.ecosystem.clinicaltrials.application;

import com.corp.ecosystem.clinicaltrials.domain.ClinicalTrialEnrollment;
import com.corp.ecosystem.clinicaltrials.domain.port.ClinicalTrialRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ClinicalTrialsZkService {

    private final ClinicalTrialRepositoryPort repositoryPort;

    public ClinicalTrialsZkService(ClinicalTrialRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public ClinicalTrialEnrollment enrollPatientWithZkProof(
            String tenantId,
            String protocolCode,
            String anonymousPatientHash,
            boolean isAgeValid,
            boolean isBiomarkerPositive,
            boolean isExclusionSatisfied
    ) {
        ClinicalTrialEnrollment.EnrollmentId id = new ClinicalTrialEnrollment.EnrollmentId("TRIAL-ENROLL-" + System.nanoTime());
        ClinicalTrialEnrollment enrollment = ClinicalTrialEnrollment.matchPatient(
                id, tenantId, protocolCode, anonymousPatientHash, isAgeValid, isBiomarkerPositive, isExclusionSatisfied
        );
        return repositoryPort.save(enrollment);
    }

    public Optional<ClinicalTrialEnrollment> getEnrollment(ClinicalTrialEnrollment.EnrollmentId id) {
        return repositoryPort.findById(id);
    }
}
