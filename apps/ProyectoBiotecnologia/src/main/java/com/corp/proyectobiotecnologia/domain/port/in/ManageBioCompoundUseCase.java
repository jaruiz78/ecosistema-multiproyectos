package com.corp.proyectobiotecnologia.domain.port.in;

import com.corp.proyectobiotecnologia.domain.model.BioCompound;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageBioCompoundUseCase {
    BioCompound createBioCompound(String tenantId, String title, double value);
    Optional<BioCompound> findBioCompoundById(String id, String tenantId);
    BioCompound processOptimization(String id, String tenantId);
}
