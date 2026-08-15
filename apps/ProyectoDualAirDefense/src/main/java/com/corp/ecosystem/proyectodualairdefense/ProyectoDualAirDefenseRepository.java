package com.corp.ecosystem.proyectodualairdefense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoDualAirDefenseRepository extends JpaRepository<ProyectoDualAirDefenseEntity, UUID> {
    List<ProyectoDualAirDefenseEntity> findByTenantId(String tenantId);
}
