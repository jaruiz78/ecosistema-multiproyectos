package com.corp.ecosystem.proyectoastroturismostarlight;
import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "proyectoastroturismostarlight_data")
public class ProyectoAstroturismoStarlightEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String tenantId;
    private double predictiveScore;
    private Instant createdAt = Instant.now();
    
    public ProyectoAstroturismoStarlightEntity() {}
    public ProyectoAstroturismoStarlightEntity(String tenantId, double predictiveScore) {
        this.tenantId = tenantId;
        this.predictiveScore = predictiveScore;
    }
    public UUID getId() { return id; }
    public String getTenantId() { return tenantId; }
    public double getPredictiveScore() { return predictiveScore; }
}
