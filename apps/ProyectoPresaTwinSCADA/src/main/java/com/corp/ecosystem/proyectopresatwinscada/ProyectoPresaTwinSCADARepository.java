package com.corp.ecosystem.proyectopresatwinscada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoPresaTwinSCADARepository extends JpaRepository<ProyectoPresaTwinSCADAEntity, UUID> {
    List<ProyectoPresaTwinSCADAEntity> findByTenantId(String tenantId);
}
