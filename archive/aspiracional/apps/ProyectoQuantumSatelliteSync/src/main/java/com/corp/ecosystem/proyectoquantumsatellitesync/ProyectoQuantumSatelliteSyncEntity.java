package com.corp.ecosystem.proyectoquantumsatellitesync;
import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "proyectoquantumsatellitesync_data")
public class ProyectoQuantumSatelliteSyncEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String tenantId;
    private double predictiveScore;
    private Instant createdAt = Instant.now();
    
    public ProyectoQuantumSatelliteSyncEntity() {}
    public ProyectoQuantumSatelliteSyncEntity(String tenantId, double predictiveScore) {
        this.tenantId = tenantId;
        this.predictiveScore = predictiveScore;
    }
    public UUID getId() { return id; }
    public String getTenantId() { return tenantId; }
    public double getPredictiveScore() { return predictiveScore; }
}
