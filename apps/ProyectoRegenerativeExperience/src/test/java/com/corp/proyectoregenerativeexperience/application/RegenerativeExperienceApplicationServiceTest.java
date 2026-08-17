package com.corp.proyectoregenerativeexperience.application;

import com.corp.proyectoregenerativeexperience.application.service.RegenerativeExperienceApplicationService;
import com.corp.proyectoregenerativeexperience.domain.model.RegenerativeExperience;
import com.corp.proyectoregenerativeexperience.infrastructure.adapter.out.persistence.InMemoryRegenerativeExperienceRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RegenerativeExperienceApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de RegenerativeExperience usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryRegenerativeExperienceRepositoryAdapter repo = new InMemoryRegenerativeExperienceRepositoryAdapter();
        RegenerativeExperienceApplicationService service = new RegenerativeExperienceApplicationService(repo);

        RegenerativeExperience created = service.createRegenerativeExperience("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<RegenerativeExperience> found = service.findRegenerativeExperienceById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        RegenerativeExperience optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
