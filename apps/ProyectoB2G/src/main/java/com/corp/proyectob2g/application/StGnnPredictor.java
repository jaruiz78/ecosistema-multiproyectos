package com.corp.proyectob2g.application;
import com.corp.proyectob2g.domain.automata.CellularState;
import com.corp.proyectob2g.domain.privacy.ZeroPiiEntity;
import java.util.List;
import java.util.stream.Collectors;

public class StGnnPredictor {
    public List<CellularState> predictSaturation(List<CellularState> grid, List<ZeroPiiEntity> entities) {
        long validEntities = entities.stream().filter(ZeroPiiEntity::isValid).count();
        return grid.stream()
            .map(c -> c.propagate((int) validEntities))
            .collect(Collectors.toList());
    }
}
