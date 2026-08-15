package com.corp.ecosystem.proyectoecotasasoberanatax;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoEcotasaSoberanaTaxRepository extends JpaRepository<ProyectoEcotasaSoberanaTaxEntity, UUID> {
    List<ProyectoEcotasaSoberanaTaxEntity> findByTenantId(String tenantId);
}
