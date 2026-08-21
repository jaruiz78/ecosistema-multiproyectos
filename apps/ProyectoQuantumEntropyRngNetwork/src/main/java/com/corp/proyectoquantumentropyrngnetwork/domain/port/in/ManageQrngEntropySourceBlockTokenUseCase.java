package com.corp.proyectoquantumentropyrngnetwork.domain.port.in;

import com.corp.proyectoquantumentropyrngnetwork.domain.model.QrngEntropySourceBlockToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageQrngEntropySourceBlockTokenUseCase {
    QrngEntropySourceBlockToken createQrngEntropySourceBlockToken(String tenantId, String title, double value);
    Optional<QrngEntropySourceBlockToken> findQrngEntropySourceBlockTokenById(String id, String tenantId);
    QrngEntropySourceBlockToken processOptimization(String id, String tenantId);
}
