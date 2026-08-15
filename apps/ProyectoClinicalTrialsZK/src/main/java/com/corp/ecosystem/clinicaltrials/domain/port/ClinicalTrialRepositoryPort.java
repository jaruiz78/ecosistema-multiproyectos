package com.corp.ecosystem.clinicaltrials.domain.port;

import com.corp.ecosystem.clinicaltrials.domain.ClinicalTrialEnrollment;
import java.util.Optional;

public interface ClinicalTrialRepositoryPort {
    ClinicalTrialEnrollment save(ClinicalTrialEnrollment enrollment);
    Optional<ClinicalTrialEnrollment> findById(ClinicalTrialEnrollment.EnrollmentId id);
}
