package com.corp.proyectoclinicalomicsmultitenant.domain.port.out;

import com.corp.proyectoclinicalomicsmultitenant.domain.model.GenomicVariantRecord;
import java.util.List;
import java.util.Optional;

public interface GenomicVariantRepositoryPort {
    GenomicVariantRecord save(GenomicVariantRecord record);
    Optional<GenomicVariantRecord> findById(String variantId);
    List<GenomicVariantRecord> findByHospital(String hospitalId);
}
