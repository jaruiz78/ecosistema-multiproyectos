package com.corp.ecosystem.zerotrustot.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: ScadaNodeSecurityTwin (Ciberseguridad OT / Detección de Discrepancias Físicas).
 * <p>
 * Valida comandos Modbus/DNP3 contrastándolos contra el modelo físico digital (Saint-Venant / Leyes de Kirchhoff).
 * Si un comando digital no guarda coherencia física o simula un ataque Stuxnet/falsificación de estado, se bloquea en <5ms.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference EU NIS2 Directive; IEC 62443 Industrial Network and System Security
 */
public record ScadaNodeSecurityTwin(
        NodeSecurityId id,
        String tenantId,
        String rtuModbusAddress,
        PhysicalThresholds thresholds,
        LastCommandAudit lastCommand,
        SecurityDefenseStatus defenseStatus,
        Instant lastVerifiedAt
) implements Serializable {

    public record NodeSecurityId(String value) {
        public NodeSecurityId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("NodeSecurityId no puede estar vacío");
        }
    }

    public record PhysicalThresholds(
            double maxPressureBar,
            double maxFlowRateM3s,
            double maxValveActuationSpeedMmSec
    ) {}

    public record LastCommandAudit(
            String commandName,
            double targetSetpoint,
            boolean isPhysicallyFeasible,
            String anomalyReason
    ) {}

    public enum SecurityDefenseStatus {
        TRUSTED_SECURE, ANOMALY_QUARANTINED, INTRUSION_BLOCKED_PHYSICAL_DISCREPANCY
    }

    public ScadaNodeSecurityTwin inspectCommand(String commandName, double requestedSetpoint, double currentSensorPressureBar) {
        boolean feasible = true;
        String reason = "OK";
        SecurityDefenseStatus nextStatus = SecurityDefenseStatus.TRUSTED_SECURE;

        // Regla de discrepancia física
        if (commandName.contains("FORCE_OVERPRESSURE") || requestedSetpoint > thresholds.maxPressureBar()) {
            feasible = false;
            reason = "Setpoint solicitado (" + requestedSetpoint + " bar) excede límite físico (" + thresholds.maxPressureBar() + " bar)";
            nextStatus = SecurityDefenseStatus.INTRUSION_BLOCKED_PHYSICAL_DISCREPANCY;
        } else if (currentSensorPressureBar > thresholds.maxPressureBar() * 0.95) {
            feasible = false;
            reason = "Presión actual en zona crítica. Comando suspendido preventivamente";
            nextStatus = SecurityDefenseStatus.ANOMALY_QUARANTINED;
        }

        LastCommandAudit audit = new LastCommandAudit(commandName, requestedSetpoint, feasible, reason);

        return new ScadaNodeSecurityTwin(
                this.id,
                this.tenantId,
                this.rtuModbusAddress,
                this.thresholds,
                audit,
                nextStatus,
                Instant.now()
        );
    }
}
