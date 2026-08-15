package com.corp.ecosystem.mice.domain.port;

import com.corp.ecosystem.mice.domain.MiceEventCongressTwin;
import java.util.Optional;

public interface MiceCongressRepositoryPort {
    MiceEventCongressTwin save(MiceEventCongressTwin event);
    Optional<MiceEventCongressTwin> findById(MiceEventCongressTwin.CongressId id);
}
