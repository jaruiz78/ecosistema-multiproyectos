package com.corp.coredigitallaw.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record ComplianceAuditResult(
        String auditId,
        String regulationFramework,
        boolean compliant,
        List<String> findings
) implements Serializable {}
