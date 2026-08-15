package com.corp.ecosystem.proyectoairlineinterlinebaggage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoAirlineInterlineBaggageRepository extends JpaRepository<ProyectoAirlineInterlineBaggageEntity, UUID> {
    List<ProyectoAirlineInterlineBaggageEntity> findByTenantId(String tenantId);
}
