package com.corp.proyectoclinicalomicsmultitenant.application.service;

import com.corp.proyectoclinicalomicsmultitenant.domain.model.GenomicVariantRecord;
import com.corp.proyectoclinicalomicsmultitenant.domain.port.out.GenomicVariantRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GenomicBiomarkerService {

    private final GenomicVariantRepositoryPort repositoryPort;

    public GenomicBiomarkerService(GenomicVariantRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public GenomicVariantRecord evaluateVariantSignificance(String variantId, String hospitalId, double populationFrequency) {
        GenomicVariantRecord record = repositoryPort.findById(variantId)
                .orElseGet(() -> GenomicVariantRecord.create(variantId, hospitalId, "chr17", 41276045, "C", "T"));

        GenomicVariantRecord.ClinicalSignificance sig;
        if (populationFrequency < 0.0001) {
            sig = GenomicVariantRecord.ClinicalSignificance.LIKELY_PATHOGENIC;
        } else if (populationFrequency > 0.05) {
            sig = GenomicVariantRecord.ClinicalSignificance.BENIGN;
        } else {
            sig = GenomicVariantRecord.ClinicalSignificance.UNCERTAIN_SIGNIFICANCE;
        }

        GenomicVariantRecord updated = record.reclassify(sig, populationFrequency);
        return repositoryPort.save(updated);
    }
}
