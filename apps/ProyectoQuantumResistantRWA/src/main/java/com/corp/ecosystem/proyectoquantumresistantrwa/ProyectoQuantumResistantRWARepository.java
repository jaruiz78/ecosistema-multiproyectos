package com.corp.ecosystem.proyectoquantumresistantrwa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoQuantumResistantRWARepository extends JpaRepository<ProyectoQuantumResistantRWAEntity, UUID> {
    List<ProyectoQuantumResistantRWAEntity> findByTenantId(String tenantId);
}
