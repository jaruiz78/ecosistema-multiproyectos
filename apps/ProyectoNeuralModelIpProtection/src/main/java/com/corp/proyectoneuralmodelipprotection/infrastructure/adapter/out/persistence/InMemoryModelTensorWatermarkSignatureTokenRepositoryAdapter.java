package com.corp.proyectoneuralmodelipprotection.infrastructure.adapter.out.persistence;

import com.corp.proyectoneuralmodelipprotection.domain.model.ModelTensorWatermarkSignatureToken;
import com.corp.proyectoneuralmodelipprotection.domain.port.out.ModelTensorWatermarkSignatureTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryModelTensorWatermarkSignatureTokenRepositoryAdapter implements ModelTensorWatermarkSignatureTokenRepositoryPort {

    private final ConcurrentMap<String, ModelTensorWatermarkSignatureToken> storage = new ConcurrentHashMap<>();

    @Override
    public ModelTensorWatermarkSignatureToken save(ModelTensorWatermarkSignatureToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ModelTensorWatermarkSignatureToken> findById(String id, String tenantId) {
        ModelTensorWatermarkSignatureToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
