package com.corp.ecosystem.proyectoglobalcruisemrv;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoGlobalCruiseMRVRepository extends JpaRepository<ProyectoGlobalCruiseMRVEntity, UUID> {
    List<ProyectoGlobalCruiseMRVEntity> findByTenantId(String tenantId);
}
