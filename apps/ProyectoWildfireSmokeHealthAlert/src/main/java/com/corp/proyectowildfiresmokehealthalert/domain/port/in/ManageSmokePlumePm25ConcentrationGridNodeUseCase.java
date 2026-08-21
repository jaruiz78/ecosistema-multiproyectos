package com.corp.proyectowildfiresmokehealthalert.domain.port.in;

import com.corp.proyectowildfiresmokehealthalert.domain.model.SmokePlumePm25ConcentrationGridNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageSmokePlumePm25ConcentrationGridNodeUseCase {
    SmokePlumePm25ConcentrationGridNode createSmokePlumePm25ConcentrationGridNode(String tenantId, String title, double value);
    Optional<SmokePlumePm25ConcentrationGridNode> findSmokePlumePm25ConcentrationGridNodeById(String id, String tenantId);
    SmokePlumePm25ConcentrationGridNode processOptimization(String id, String tenantId);
}
