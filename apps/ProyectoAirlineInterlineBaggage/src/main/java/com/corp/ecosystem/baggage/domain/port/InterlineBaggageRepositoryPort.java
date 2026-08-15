package com.corp.ecosystem.baggage.domain.port;

import com.corp.ecosystem.baggage.domain.InterlineBaggageTwin;
import java.util.Optional;

public interface InterlineBaggageRepositoryPort {
    InterlineBaggageTwin save(InterlineBaggageTwin twin);
    Optional<InterlineBaggageTwin> findById(InterlineBaggageTwin.BaggageTagId id);
}
