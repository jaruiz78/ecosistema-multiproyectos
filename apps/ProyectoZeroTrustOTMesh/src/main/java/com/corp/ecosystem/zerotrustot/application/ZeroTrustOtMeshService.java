package com.corp.ecosystem.zerotrustot.application;

import com.corp.ecosystem.zerotrustot.domain.ScadaNodeSecurityTwin;
import com.corp.ecosystem.zerotrustot.domain.port.ScadaSecurityRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class ZeroTrustOtMeshService {

    private final ScadaSecurityRepositoryPort repositoryPort;

    public ZeroTrustOtMeshService(ScadaSecurityRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public ScadaNodeSecurityTwin registerScadaNode(
            String tenantId,
            String modbusAddress,
            double maxPressureBar,
            double maxFlowM3s
    ) {
        ScadaNodeSecurityTwin.PhysicalThresholds thresholds = new ScadaNodeSecurityTwin.PhysicalThresholds(
                maxPressureBar, maxFlowM3s, 10.0
        );
        ScadaNodeSecurityTwin.LastCommandAudit audit = new ScadaNodeSecurityTwin.LastCommandAudit(
                "INITIALIZE", 0.0, true, "OK"
        );

        ScadaNodeSecurityTwin node = new ScadaNodeSecurityTwin(
                new ScadaNodeSecurityTwin.NodeSecurityId("OT-NODE-" + System.nanoTime()),
                tenantId,
                modbusAddress,
                thresholds,
                audit,
                ScadaNodeSecurityTwin.SecurityDefenseStatus.TRUSTED_SECURE,
                Instant.now()
        );
        return repositoryPort.save(node);
    }

    public ScadaNodeSecurityTwin inspectAndFilterCommand(
            ScadaNodeSecurityTwin.NodeSecurityId id,
            String commandName,
            double targetSetpoint,
            double currentPressureBar
    ) {
        ScadaNodeSecurityTwin node = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nodo SCADA no encontrado: " + id.value()));

        ScadaNodeSecurityTwin updated = node.inspectCommand(commandName, targetSetpoint, currentPressureBar);
        return repositoryPort.save(updated);
    }

    public Optional<ScadaNodeSecurityTwin> getNode(ScadaNodeSecurityTwin.NodeSecurityId id) {
        return repositoryPort.findById(id);
    }
}
