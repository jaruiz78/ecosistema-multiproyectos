package com.corp.ecosystem.proyectocatastrofes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoCatastrofesRepository extends JpaRepository<ProyectoCatastrofesEntity, UUID> {
    List<ProyectoCatastrofesEntity> findByTenantId(String tenantId);
}
