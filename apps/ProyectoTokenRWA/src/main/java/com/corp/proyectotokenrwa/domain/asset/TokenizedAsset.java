package com.corp.proyectotokenrwa.domain.asset;
import java.math.BigDecimal;
public record TokenizedAsset(String assetId, String type, BigDecimal value, boolean isLocked) {
    public TokenizedAsset lock() { return new TokenizedAsset(assetId, type, value, true); }
}
