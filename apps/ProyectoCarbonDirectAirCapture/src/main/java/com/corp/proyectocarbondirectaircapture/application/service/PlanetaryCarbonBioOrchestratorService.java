package com.corp.proyectocarbondirectaircapture.application.service;

import com.corp.corehyperspectral.application.SatelliteHyperspectralAnalysisUseCase;
import com.corp.corehyperspectral.domain.HyperspectralMineralSignature;
import com.corp.proyectobiodiversitygenomicbank.domain.model.EnvironmentalDnaSample;
import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;

import java.io.Serializable;
import java.util.Map;

/**
 * Servicio de orquestación sinérgica planetaria que integra:
 * 1. Teledetección hiperespectral (N-FINDR) para detección de formaciones basálticas reactivas.
 * 2. Captura directa de aire (DAC) y mineralización geológica acelerada de CO2.
 * 3. Monitorización metagenómica de biodiversidad (eDNA) y emisión de bio-créditos.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlanetaryCarbonBioOrchestratorService implements Serializable {

    private final SatelliteHyperspectralAnalysisUseCase hyperspectralUseCase = new SatelliteHyperspectralAnalysisUseCase();

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CarbonBioLoopResult(
            String facilityId,
            long h3Location,
            double capturedCo2Tonnes,
            double mineralizedCo2Tonnes,
            double shannonDiversityIndexH,
            boolean carbonNegativeVerified
    ) implements Serializable {}

    public CarbonBioLoopResult executeCarbonMineralizationAndBioMonitoring(
            DirectAirCaptureFacility dacFacility,
            long h3Location,
            double batchCo2Tonnes,
            double[] pixelSpectra,
            double[][] basaltLibrary,
            Map<String, Integer> eDnaSpeciesReads
    ) {
        // 1. Verificar yacimiento de basalto reactivo mediante satélite
        HyperspectralMineralSignature signature = hyperspectralUseCase.processSentinelPrismaScene(
                "SCENE-" + dacFacility.facilityId(), h3Location, pixelSpectra, basaltLibrary
        );

        // 2. Inyectar y mineralizar lote de CO2
        DirectAirCaptureFacility updatedDac = dacFacility.recordMineralizationBatch(batchCo2Tonnes);
        double netMineralized = batchCo2Tonnes * (updatedDac.mineralizationEfficiencyPct() / 100.0);

        // 3. Evaluar impacto en la biodiversidad del bioma mediante eDNA
        EnvironmentalDnaSample bioSample = EnvironmentalDnaSample.create(
                "EDNA-" + dacFacility.facilityId(), "BASALTIC_SAVANNA", h3Location, eDnaSpeciesReads
        );

        boolean verified = netMineralized > 0.0 && signature.soilCarbonIndex() >= 0.0 && bioSample.shannonDiversityIndexH() > 0.5;

        return new CarbonBioLoopResult(
                dacFacility.facilityId(),
                h3Location,
                batchCo2Tonnes,
                netMineralized,
                bioSample.shannonDiversityIndexH(),
                verified
        );
    }
}
