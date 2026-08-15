package com.corp.ecosystem.pharmacold.domain.port;

import com.corp.ecosystem.pharmacold.domain.PharmaShipmentBatch;
import java.util.Optional;

public interface PharmaBatchRepositoryPort {
    PharmaShipmentBatch save(PharmaShipmentBatch batch);
    Optional<PharmaShipmentBatch> findById(PharmaShipmentBatch.BatchId id);
}
