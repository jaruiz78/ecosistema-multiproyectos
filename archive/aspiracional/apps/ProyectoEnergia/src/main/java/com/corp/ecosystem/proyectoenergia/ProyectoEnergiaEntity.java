package com.corp.ecosystem.proyectoenergia;
import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "proyectoenergia_data")
public class ProyectoEnergiaEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String tenantId;
    private double predictiveScore;
    private Instant createdAt = Instant.now();
    
    public ProyectoEnergiaEntity() {}
    public ProyectoEnergiaEntity(String tenantId, double predictiveScore) {
        this.tenantId = tenantId;
        this.predictiveScore = predictiveScore;
    }
    public UUID getId() { return id; }
    public String getTenantId() { return tenantId; }
    public double getPredictiveScore() { return predictiveScore; }
}
