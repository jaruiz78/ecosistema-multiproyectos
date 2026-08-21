package com.corp.proyectoparametricweatherinsurance.infrastructure.adapter.out.persistence;

import com.corp.proyectoparametricweatherinsurance.domain.model.ParametricInsurancePolicyPayoutToken;
import com.corp.proyectoparametricweatherinsurance.domain.port.out.ParametricInsurancePolicyPayoutTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryParametricInsurancePolicyPayoutTokenRepositoryAdapter implements ParametricInsurancePolicyPayoutTokenRepositoryPort {

    private final ConcurrentMap<String, ParametricInsurancePolicyPayoutToken> storage = new ConcurrentHashMap<>();

    @Override
    public ParametricInsurancePolicyPayoutToken save(ParametricInsurancePolicyPayoutToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ParametricInsurancePolicyPayoutToken> findById(String id, String tenantId) {
        ParametricInsurancePolicyPayoutToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
