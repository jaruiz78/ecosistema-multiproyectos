package com.corp.ecosystem.proyectoindustrialmicrogridmpc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoIndustrialMicrogridMPCRepository extends JpaRepository<ProyectoIndustrialMicrogridMPCEntity, UUID> {
    List<ProyectoIndustrialMicrogridMPCEntity> findByTenantId(String tenantId);
}
