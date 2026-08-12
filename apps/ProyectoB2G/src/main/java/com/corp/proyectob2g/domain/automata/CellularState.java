package com.corp.proyectob2g.domain.automata;
public record CellularState(String cellId, int saturationLevel) {
    public CellularState propagate(int externalLoad) {
        return new CellularState(cellId, Math.min(100, saturationLevel + externalLoad));
    }
}
