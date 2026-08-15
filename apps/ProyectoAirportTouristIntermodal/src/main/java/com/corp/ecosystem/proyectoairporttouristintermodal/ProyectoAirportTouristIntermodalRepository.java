package com.corp.ecosystem.proyectoairporttouristintermodal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoAirportTouristIntermodalRepository extends JpaRepository<ProyectoAirportTouristIntermodalEntity, UUID> {
    List<ProyectoAirportTouristIntermodalEntity> findByTenantId(String tenantId);
}
