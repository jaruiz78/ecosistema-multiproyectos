package com.corp.proyectoclinicalomicsmultitenant.infrastructure.adapter.out.persistence;

import com.corp.proyectoclinicalomicsmultitenant.domain.model.GenomicVariantRecord;
import com.corp.proyectoclinicalomicsmultitenant.domain.port.out.GenomicVariantRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryGenomicVariantRepositoryAdapter implements GenomicVariantRepositoryPort {

    private final Map<String, GenomicVariantRecord> database = new ConcurrentHashMap<>();

    @Override
    public GenomicVariantRecord save(GenomicVariantRecord record) {
        database.put(record.variantId(), record);
        return record;
    }

    @Override
    public Optional<GenomicVariantRecord> findById(String variantId) {
        return Optional.ofNullable(database.get(variantId));
    }

    @Override
    public List<GenomicVariantRecord> findByHospital(String hospitalId) {
        return database.values().stream()
                .filter(r -> r.tenantHospitalId().equals(hospitalId))
                .toList();
    }
}
