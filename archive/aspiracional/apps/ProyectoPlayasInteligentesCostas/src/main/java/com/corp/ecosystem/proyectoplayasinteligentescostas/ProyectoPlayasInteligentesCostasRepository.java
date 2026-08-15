package com.corp.ecosystem.proyectoplayasinteligentescostas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoPlayasInteligentesCostasRepository extends JpaRepository<ProyectoPlayasInteligentesCostasEntity, UUID> {
    List<ProyectoPlayasInteligentesCostasEntity> findByTenantId(String tenantId);
}
