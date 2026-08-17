package com.corp.proyectoagrobiorobotics.application;

import com.corp.proyectoagrobiorobotics.application.service.AgroBioRoboticsApplicationService;
import com.corp.proyectoagrobiorobotics.domain.model.AgroBioRobotics;
import com.corp.proyectoagrobiorobotics.infrastructure.adapter.out.persistence.InMemoryAgroBioRoboticsRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AgroBioRoboticsApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de AgroBioRobotics usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryAgroBioRoboticsRepositoryAdapter repo = new InMemoryAgroBioRoboticsRepositoryAdapter();
        AgroBioRoboticsApplicationService service = new AgroBioRoboticsApplicationService(repo);

        AgroBioRobotics created = service.createAgroBioRobotics("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<AgroBioRobotics> found = service.findAgroBioRoboticsById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        AgroBioRobotics optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
