package com.corp.ecosystem.proyectoparquesnacionalesnatura2000;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProyectoParquesNacionalesNatura2000Repository extends JpaRepository<ProyectoParquesNacionalesNatura2000Entity, UUID> {
    List<ProyectoParquesNacionalesNatura2000Entity> findByTenantId(String tenantId);
}
