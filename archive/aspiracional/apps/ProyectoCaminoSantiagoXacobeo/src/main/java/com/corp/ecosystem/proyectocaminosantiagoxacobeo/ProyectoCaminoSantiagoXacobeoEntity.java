package com.corp.ecosystem.proyectocaminosantiagoxacobeo;
import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "proyectocaminosantiagoxacobeo_data")
public class ProyectoCaminoSantiagoXacobeoEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String tenantId;
    private double predictiveScore;
    private Instant createdAt = Instant.now();
    
    public ProyectoCaminoSantiagoXacobeoEntity() {}
    public ProyectoCaminoSantiagoXacobeoEntity(String tenantId, double predictiveScore) {
        this.tenantId = tenantId;
        this.predictiveScore = predictiveScore;
    }
    public UUID getId() { return id; }
    public String getTenantId() { return tenantId; }
    public double getPredictiveScore() { return predictiveScore; }
}
