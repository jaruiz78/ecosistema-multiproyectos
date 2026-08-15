package com.corp.ecosystem.proyectocaminosantiagoxacobeo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoCaminoSantiagoXacobeoRepository extends JpaRepository<ProyectoCaminoSantiagoXacobeoEntity, UUID> {
    List<ProyectoCaminoSantiagoXacobeoEntity> findByTenantId(String tenantId);
}
