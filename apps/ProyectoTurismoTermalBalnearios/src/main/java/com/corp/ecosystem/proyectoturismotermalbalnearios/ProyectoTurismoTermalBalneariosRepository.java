package com.corp.ecosystem.proyectoturismotermalbalnearios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoTurismoTermalBalneariosRepository extends JpaRepository<ProyectoTurismoTermalBalneariosEntity, UUID> {
    List<ProyectoTurismoTermalBalneariosEntity> findByTenantId(String tenantId);
}
