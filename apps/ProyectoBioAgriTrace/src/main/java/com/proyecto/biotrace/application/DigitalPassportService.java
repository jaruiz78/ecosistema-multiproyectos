package com.proyecto.biotrace.application;

import com.proyecto.biotrace.domain.ProductPassport;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Servicio de emisión y certificación de Pasaportes Digitales de Producto (DPP).
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class DigitalPassportService {

    private static final HexFormat HEX = HexFormat.of();

    public ProductPassport issueDigitalPassport(String batchId, String h3PlotCell, String cropType, double waterLitersPerKg, double carbonGramsCo2PerKg, boolean zeroChemicalResidue) {
        String rawContent = batchId + ":" + h3PlotCell + ":" + cropType + ":" + waterLitersPerKg + ":" + carbonGramsCo2PerKg + ":" + zeroChemicalResidue;
        String qrDigest = computeSha256("DPP_EU_2026_PASSPORT:" + rawContent);

        return new ProductPassport(batchId, h3PlotCell, cropType, waterLitersPerKg, carbonGramsCo2PerKg, zeroChemicalResidue, qrDigest);
    }

    private static String computeSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HEX.formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
