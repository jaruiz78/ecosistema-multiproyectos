package com.corp.ecosystem.proyectoecotourismpassport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoEcoTourismPassportRepository extends JpaRepository<ProyectoEcoTourismPassportEntity, UUID> {
    List<ProyectoEcoTourismPassportEntity> findByTenantId(String tenantId);
}
