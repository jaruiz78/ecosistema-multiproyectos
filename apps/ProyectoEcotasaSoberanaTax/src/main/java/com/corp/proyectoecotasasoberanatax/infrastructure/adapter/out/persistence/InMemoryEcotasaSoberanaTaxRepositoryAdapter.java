package com.corp.proyectoecotasasoberanatax.infrastructure.adapter.out.persistence;

import com.corp.proyectoecotasasoberanatax.domain.model.EcotasaSoberanaTax;
import com.corp.proyectoecotasasoberanatax.domain.port.out.EcotasaSoberanaTaxRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryEcotasaSoberanaTaxRepositoryAdapter implements EcotasaSoberanaTaxRepositoryPort {

    private final ConcurrentMap<String, EcotasaSoberanaTax> storage = new ConcurrentHashMap<>();

    @Override
    public EcotasaSoberanaTax save(EcotasaSoberanaTax entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EcotasaSoberanaTax> findById(String id, String tenantId) {
        EcotasaSoberanaTax entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
