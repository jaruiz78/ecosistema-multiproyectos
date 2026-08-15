package com.corp.ecosystem.proyectocriticalmineralsmrv;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoCriticalMineralsMRVRepository extends JpaRepository<ProyectoCriticalMineralsMRVEntity, UUID> {
    List<ProyectoCriticalMineralsMRVEntity> findByTenantId(String tenantId);
}
