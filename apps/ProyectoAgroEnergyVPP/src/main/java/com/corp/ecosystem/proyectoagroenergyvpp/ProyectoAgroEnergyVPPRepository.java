package com.corp.ecosystem.proyectoagroenergyvpp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoAgroEnergyVPPRepository extends JpaRepository<ProyectoAgroEnergyVPPEntity, UUID> {
    List<ProyectoAgroEnergyVPPEntity> findByTenantId(String tenantId);
}
