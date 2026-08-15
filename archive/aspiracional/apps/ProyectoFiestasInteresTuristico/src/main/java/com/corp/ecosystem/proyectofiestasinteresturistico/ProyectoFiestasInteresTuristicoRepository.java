package com.corp.ecosystem.proyectofiestasinteresturistico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoFiestasInteresTuristicoRepository extends JpaRepository<ProyectoFiestasInteresTuristicoEntity, UUID> {
    List<ProyectoFiestasInteresTuristicoEntity> findByTenantId(String tenantId);
}
