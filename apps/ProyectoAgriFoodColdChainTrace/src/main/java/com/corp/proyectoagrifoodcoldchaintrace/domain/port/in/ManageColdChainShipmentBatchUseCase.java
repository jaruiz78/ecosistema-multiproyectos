package com.corp.proyectoagrifoodcoldchaintrace.domain.port.in;

import com.corp.proyectoagrifoodcoldchaintrace.domain.model.ColdChainShipmentBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageColdChainShipmentBatchUseCase {
    ColdChainShipmentBatch createColdChainShipmentBatch(String tenantId, String title, double value);
    Optional<ColdChainShipmentBatch> findColdChainShipmentBatchById(String id, String tenantId);
    ColdChainShipmentBatch processOptimization(String id, String tenantId);
}
