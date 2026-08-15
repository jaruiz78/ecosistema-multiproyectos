package com.corp.ecosystem.proyectohoteltwinrevpar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoHotelTwinRevPARRepository extends JpaRepository<ProyectoHotelTwinRevPAREntity, UUID> {
    List<ProyectoHotelTwinRevPAREntity> findByTenantId(String tenantId);
}
