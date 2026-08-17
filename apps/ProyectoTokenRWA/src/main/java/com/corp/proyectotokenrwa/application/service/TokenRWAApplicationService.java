package com.corp.proyectotokenrwa.application.service;

import com.corp.proyectotokenrwa.domain.model.TokenRWA;
import com.corp.proyectotokenrwa.domain.port.in.ManageTokenRWAUseCase;
import com.corp.proyectotokenrwa.domain.port.out.TokenRWARepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class TokenRWAApplicationService implements ManageTokenRWAUseCase {

    private final TokenRWARepositoryPort repositoryPort;

    public TokenRWAApplicationService(TokenRWARepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TokenRWA createTokenRWA(String tenantId, String title, double value) {
        TokenRWA entity = new TokenRWA(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<TokenRWA> findTokenRWAById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TokenRWA processOptimization(String id, String tenantId) {
        TokenRWA existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TokenRWA optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
