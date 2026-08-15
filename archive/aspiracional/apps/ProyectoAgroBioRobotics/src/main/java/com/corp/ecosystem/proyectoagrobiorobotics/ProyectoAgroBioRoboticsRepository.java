package com.corp.ecosystem.proyectoagrobiorobotics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoAgroBioRoboticsRepository extends JpaRepository<ProyectoAgroBioRoboticsEntity, UUID> {
    List<ProyectoAgroBioRoboticsEntity> findByTenantId(String tenantId);
}
