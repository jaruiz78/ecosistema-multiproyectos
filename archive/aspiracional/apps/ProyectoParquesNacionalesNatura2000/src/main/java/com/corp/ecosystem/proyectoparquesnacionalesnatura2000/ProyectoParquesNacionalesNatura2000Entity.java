package com.corp.ecosystem.proyectoparquesnacionalesnatura2000;
import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "proyectoparquesnacionalesnatura2000_data")
public class ProyectoParquesNacionalesNatura2000Entity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String tenantId;
    private double predictiveScore;
    private Instant createdAt = Instant.now();
    
    public ProyectoParquesNacionalesNatura2000Entity() {}
    public ProyectoParquesNacionalesNatura2000Entity(String tenantId, double predictiveScore) {
        this.tenantId = tenantId;
        this.predictiveScore = predictiveScore;
    }
    public UUID getId() { return id; }
    public String getTenantId() { return tenantId; }
    public double getPredictiveScore() { return predictiveScore; }
}
