package com.corp.ecosystem.proyectocirculartextiledpp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoCircularTextileDPPRepository extends JpaRepository<ProyectoCircularTextileDPPEntity, UUID> {
    List<ProyectoCircularTextileDPPEntity> findByTenantId(String tenantId);
}
