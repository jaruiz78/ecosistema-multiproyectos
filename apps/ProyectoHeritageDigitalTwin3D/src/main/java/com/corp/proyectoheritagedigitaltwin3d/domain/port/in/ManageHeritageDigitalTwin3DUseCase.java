package com.corp.proyectoheritagedigitaltwin3d.domain.port.in;

import com.corp.proyectoheritagedigitaltwin3d.domain.model.HeritageDigitalTwin3D;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageHeritageDigitalTwin3DUseCase {
    HeritageDigitalTwin3D createHeritageDigitalTwin3D(String tenantId, String title, double value);
    Optional<HeritageDigitalTwin3D> findHeritageDigitalTwin3DById(String id, String tenantId);
    HeritageDigitalTwin3D processOptimization(String id, String tenantId);
}
