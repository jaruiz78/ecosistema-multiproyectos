package com.corp.ecosystem.proyectologistica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoLogisticaRepository extends JpaRepository<ProyectoLogisticaEntity, UUID> {
    List<ProyectoLogisticaEntity> findByTenantId(String tenantId);
}
