package com.corp.proyectotokenrwa.domain.asset;
import java.math.BigDecimal;
/**
 * Arquitectura y especificación formal para TokenizedAsset.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public record TokenizedAsset(String assetId, String type, BigDecimal value, boolean isLocked) {
    public TokenizedAsset lock() { return new TokenizedAsset(assetId, type, value, true); }
}
