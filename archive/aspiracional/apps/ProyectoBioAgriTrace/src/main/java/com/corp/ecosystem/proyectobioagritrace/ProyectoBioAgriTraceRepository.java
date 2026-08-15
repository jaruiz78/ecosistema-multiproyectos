package com.corp.ecosystem.proyectobioagritrace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoBioAgriTraceRepository extends JpaRepository<ProyectoBioAgriTraceEntity, UUID> {
    List<ProyectoBioAgriTraceEntity> findByTenantId(String tenantId);
}
