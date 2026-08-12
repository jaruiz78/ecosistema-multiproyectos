package com.corp.proyectob2g.domain.privacy;
public record ZeroPiiEntity(String anonymizedId, String region) {
    public boolean isValid() { return anonymizedId != null && anonymizedId.startsWith("anon_"); }
}
