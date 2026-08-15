package com.corp.ecosystem.proyectoenoturismorutasvino;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoEnoturismoRutasVinoRepository extends JpaRepository<ProyectoEnoturismoRutasVinoEntity, UUID> {
    List<ProyectoEnoturismoRutasVinoEntity> findByTenantId(String tenantId);
}
