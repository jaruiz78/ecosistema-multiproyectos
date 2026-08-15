package com.corp.ecosystem.proyectoclinicaltrialszk;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoClinicalTrialsZKRepository extends JpaRepository<ProyectoClinicalTrialsZKEntity, UUID> {
    List<ProyectoClinicalTrialsZKEntity> findByTenantId(String tenantId);
}
