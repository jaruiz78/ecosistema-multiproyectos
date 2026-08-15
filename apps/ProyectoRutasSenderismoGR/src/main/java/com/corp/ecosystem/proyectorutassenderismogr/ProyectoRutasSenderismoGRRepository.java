package com.corp.ecosystem.proyectorutassenderismogr;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoRutasSenderismoGRRepository extends JpaRepository<ProyectoRutasSenderismoGREntity, UUID> {
    List<ProyectoRutasSenderismoGREntity> findByTenantId(String tenantId);
}
