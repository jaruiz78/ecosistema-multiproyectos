package com.corp.ecosystem.proyectocascohistoricocrowd;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoCascoHistoricoCrowdRepository extends JpaRepository<ProyectoCascoHistoricoCrowdEntity, UUID> {
    List<ProyectoCascoHistoricoCrowdEntity> findByTenantId(String tenantId);
}
