package com.corp.corehyperspectral.application;

import com.corp.core.math.hyperspectral.LinearSpectralUnmixer;
import com.corp.core.math.hyperspectral.NFindrEndmemberExtractor;
import com.corp.corehyperspectral.domain.HyperspectralMineralSignature;

import java.util.List;

public class SatelliteHyperspectralAnalysisUseCase {

    public HyperspectralMineralSignature processSentinelPrismaScene(
            String sceneId,
            long h3Location,
            double[] targetPixelBands,
            double[][] referenceMineralLibrary
    ) {
        List<double[]> endmembers = NFindrEndmemberExtractor.extractEndmembers(referenceMineralLibrary, referenceMineralLibrary.length);
        double[][] emArray = endmembers.toArray(new double[0][]);

        double[] abundances = LinearSpectralUnmixer.estimateAbundances(targetPixelBands, emArray);
        double soilCarbon = (abundances.length > 0) ? abundances[0] * 100.0 : 0.0;
        boolean deposit = (abundances.length > 1) && abundances[1] > 0.40;

        return new HyperspectralMineralSignature(sceneId, h3Location, abundances, soilCarbon, deposit);
    }
}
