package com.corp.proyectotokenrwa.infrastructure.adapter.out.persistence;

import com.corp.proyectotokenrwa.domain.model.TokenRWA;
import com.corp.proyectotokenrwa.domain.port.out.TokenRWARepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryTokenRWARepositoryAdapter implements TokenRWARepositoryPort {

    private final ConcurrentMap<String, TokenRWA> storage = new ConcurrentHashMap<>();

    @Override
    public TokenRWA save(TokenRWA entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TokenRWA> findById(String id, String tenantId) {
        TokenRWA entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
