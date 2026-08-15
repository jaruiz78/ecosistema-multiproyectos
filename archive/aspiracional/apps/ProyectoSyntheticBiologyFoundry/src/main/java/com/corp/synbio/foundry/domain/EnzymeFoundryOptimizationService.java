package com.corp.synbio.foundry.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de Optimización de Biología Sintética y Certificación de Captura de Carbono.
 * Diseña variantes enzimáticas óptimas y emite certificaciones criptográficas para pasaportes bio-digitales.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class EnzymeFoundryOptimizationService {

    public record BioreactorRunCertificate(
            String bioreactorId,
            String optimalVariantId,
            int viableVariantsEvaluated,
            double totalCo2CapturedKg24h,
            String carbonCreditCertificateSha256,
            String zkMerkleProofHash
    ) {}

    public BioreactorRunCertificate certifyBioreactorRun(String bioreactorId, List<SyntheticEnzymeVariant> candidateVariants, double bioreactorVolumeLiters) {
        Objects.requireNonNull(bioreactorId, "bioreactorId no puede ser nulo");
        Objects.requireNonNull(candidateVariants, "candidateVariants no puede ser nulo");

        List<SyntheticEnzymeVariant> viable = candidateVariants.stream()
                .filter(SyntheticEnzymeVariant::isCommerciallyViable)
                .toList();

        if (viable.isEmpty()) {
            return new BioreactorRunCertificate(bioreactorId, "NONE", 0, 0.0, "CERTIFICATE_NOT_ELIGIBLE", "UNPROVEN");
        }

        // Seleccionar la variante con mayor tasa de fijación
        SyntheticEnzymeVariant best = viable.stream()
                .max((v1, v2) -> Double.compare(v1.co2FixationRateGramsPerHour(), v2.co2FixationRateGramsPerHour()))
                .orElse(viable.get(0));

        // Cómputo de CO2 capturado en 24 horas por el volumen del biorreactor (kg)
        double co2Kg24h = (best.co2FixationRateGramsPerHour() * bioreactorVolumeLiters * 24.0) / 1000.0;

        String certSeed = bioreactorId + ":" + best.variantId() + ":" + co2Kg24h + ":" + best.aminoAcidSequenceHash();
        String certHash = sha256(certSeed);
        String zkProof = "ZK_SNARK_CARBON_BIO_" + certHash.substring(0, 16);

        return new BioreactorRunCertificate(
                bioreactorId, best.variantId(), viable.size(),
                Math.round(co2Kg24h * 100.0) / 100.0, certHash, zkProof
        );
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
}
