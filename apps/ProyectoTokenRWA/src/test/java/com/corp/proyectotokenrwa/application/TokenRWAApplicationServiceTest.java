package com.corp.proyectotokenrwa.application;

import com.corp.proyectotokenrwa.application.service.TokenRWAApplicationService;
import com.corp.proyectotokenrwa.domain.model.TokenRWA;
import com.corp.proyectotokenrwa.infrastructure.adapter.out.persistence.InMemoryTokenRWARepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TokenRWAApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de TokenRWA usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryTokenRWARepositoryAdapter repo = new InMemoryTokenRWARepositoryAdapter();
        TokenRWAApplicationService service = new TokenRWAApplicationService(repo);

        TokenRWA created = service.createTokenRWA("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<TokenRWA> found = service.findTokenRWAById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        TokenRWA optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
