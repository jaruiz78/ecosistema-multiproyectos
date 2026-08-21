package com.corp.coredigitallaw.application;

import com.corp.core.law.compliance.EuAiActComplianceEngine;
import com.corp.coredigitallaw.domain.ComplianceAuditResult;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DigitalLawVerificationUseCase {

    public ComplianceAuditResult auditAiSystem(
            String auditId,
            String systemName,
            boolean isCriticalInfra,
            boolean hasOversight,
            boolean hasDocs,
            boolean hasRiskMgmt
    ) {
        var profile = new EuAiActComplianceEngine.SystemProfile(
                systemName,
                false,
                isCriticalInfra,
                false,
                hasOversight,
                hasDocs,
                hasRiskMgmt
        );
        var report = EuAiActComplianceEngine.evaluate(profile);

        return new ComplianceAuditResult(
                auditId,
                "EU AI Act (Regulation 2024/1689) - Risk: " + report.riskCategory(),
                report.compliant(),
                report.missingRequirements()
        );
    }
}
