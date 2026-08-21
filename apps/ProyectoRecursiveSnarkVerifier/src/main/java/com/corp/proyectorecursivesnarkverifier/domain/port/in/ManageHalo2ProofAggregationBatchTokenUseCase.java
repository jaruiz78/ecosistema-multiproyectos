package com.corp.proyectorecursivesnarkverifier.domain.port.in;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageHalo2ProofAggregationBatchTokenUseCase {
    Halo2ProofAggregationBatchToken createHalo2ProofAggregationBatchToken(String tenantId, String title, double value);
    Optional<Halo2ProofAggregationBatchToken> findHalo2ProofAggregationBatchTokenById(String id, String tenantId);
    Halo2ProofAggregationBatchToken processOptimization(String id, String tenantId);
}
