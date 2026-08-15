package com.corp.ecosystem.fiestas.domain.port;

import com.corp.ecosystem.fiestas.domain.TouristFestivalTwin;
import java.util.Optional;

public interface TouristFestivalRepositoryPort {
    TouristFestivalTwin save(TouristFestivalTwin festival);
    Optional<TouristFestivalTwin> findById(TouristFestivalTwin.FestivalId id);
}
