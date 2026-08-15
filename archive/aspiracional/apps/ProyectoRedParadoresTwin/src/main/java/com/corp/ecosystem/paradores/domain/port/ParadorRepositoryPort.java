package com.corp.ecosystem.paradores.domain.port;

import com.corp.ecosystem.paradores.domain.HistoricParadorTwin;
import java.util.Optional;

public interface ParadorRepositoryPort {
    HistoricParadorTwin save(HistoricParadorTwin parador);
    Optional<HistoricParadorTwin> findById(HistoricParadorTwin.ParadorId id);
}
