package com.corp.proyectodnaarchivaldatastorage.infrastructure.adapter.out.persistence;

import com.corp.proyectodnaarchivaldatastorage.domain.model.DnaOligonucleotideDataBlockToken;
import com.corp.proyectodnaarchivaldatastorage.domain.port.out.DnaOligonucleotideDataBlockTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryDnaOligonucleotideDataBlockTokenRepositoryAdapter implements DnaOligonucleotideDataBlockTokenRepositoryPort {

    private final ConcurrentMap<String, DnaOligonucleotideDataBlockToken> storage = new ConcurrentHashMap<>();

    @Override
    public DnaOligonucleotideDataBlockToken save(DnaOligonucleotideDataBlockToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DnaOligonucleotideDataBlockToken> findById(String id, String tenantId) {
        DnaOligonucleotideDataBlockToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
