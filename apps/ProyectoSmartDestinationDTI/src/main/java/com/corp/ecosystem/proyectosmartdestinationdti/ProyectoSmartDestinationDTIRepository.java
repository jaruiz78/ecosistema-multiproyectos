package com.corp.ecosystem.proyectosmartdestinationdti;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoSmartDestinationDTIRepository extends JpaRepository<ProyectoSmartDestinationDTIEntity, UUID> {
    List<ProyectoSmartDestinationDTIEntity> findByTenantId(String tenantId);
}
