package com.corp.ecosystem.proyectoagua;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoAguaRepository extends JpaRepository<ProyectoAguaEntity, UUID> {
    List<ProyectoAguaEntity> findByTenantId(String tenantId);
}
