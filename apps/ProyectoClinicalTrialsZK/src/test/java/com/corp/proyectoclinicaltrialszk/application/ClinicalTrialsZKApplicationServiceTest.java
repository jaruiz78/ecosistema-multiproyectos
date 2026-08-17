package com.corp.proyectoclinicaltrialszk.application;

import com.corp.proyectoclinicaltrialszk.application.service.ClinicalTrialsZKApplicationService;
import com.corp.proyectoclinicaltrialszk.domain.model.ClinicalTrialsZK;
import com.corp.proyectoclinicaltrialszk.infrastructure.adapter.out.persistence.InMemoryClinicalTrialsZKRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ClinicalTrialsZKApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de ClinicalTrialsZK usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryClinicalTrialsZKRepositoryAdapter repo = new InMemoryClinicalTrialsZKRepositoryAdapter();
        ClinicalTrialsZKApplicationService service = new ClinicalTrialsZKApplicationService(repo);

        ClinicalTrialsZK created = service.createClinicalTrialsZK("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<ClinicalTrialsZK> found = service.findClinicalTrialsZKById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        ClinicalTrialsZK optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
