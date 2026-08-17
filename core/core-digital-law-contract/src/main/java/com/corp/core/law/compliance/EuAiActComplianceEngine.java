package com.corp.core.law.compliance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Motor determinista de clasificación y verificación de requisitos bajo el Reglamento Europeo de IA (EU AI Act - Regulation (EU) 2024/1689).
 */
public record EuAiActComplianceEngine() implements Serializable {

    public enum RiskCategory {
        UNACCEPTABLE_RISK,
        HIGH_RISK,
        SPECIFIC_TRANSPARENCY_RISK,
        MINIMAL_RISK
    }

    public record SystemProfile(
            String systemName,
            boolean usesBiometricIdentification,
            boolean criticalInfrastructureManagement,
            boolean generatesSyntheticContent,
            boolean hasHumanOversightProtocol,
            boolean hasDetailedTechnicalDocumentation,
            boolean hasContinuousRiskManagement
    ) implements Serializable {}

    public record ComplianceReport(
            RiskCategory riskCategory,
            boolean compliant,
            List<String> missingRequirements
    ) implements Serializable {}

    public static ComplianceReport evaluate(SystemProfile profile) {
        List<String> missing = new ArrayList<>();
        RiskCategory category;

        if (profile.usesBiometricIdentification()) {
            category = RiskCategory.HIGH_RISK;
        } else if (profile.criticalInfrastructureManagement()) {
            category = RiskCategory.HIGH_RISK;
        } else if (profile.generatesSyntheticContent()) {
            category = RiskCategory.SPECIFIC_TRANSPARENCY_RISK;
        } else {
            category = RiskCategory.MINIMAL_RISK;
        }

        if (category == RiskCategory.HIGH_RISK) {
            if (!profile.hasHumanOversightProtocol()) {
                missing.add("Art. 14: Supervisión humana no implementada");
            }
            if (!profile.hasDetailedTechnicalDocumentation()) {
                missing.add("Art. 11: Documentación técnica detallada ausente");
            }
            if (!profile.hasContinuousRiskManagement()) {
                missing.add("Art. 9: Sistema de gestión de riesgos continuo ausente");
            }
        }

        return new ComplianceReport(category, missing.isEmpty(), missing);
    }
}
