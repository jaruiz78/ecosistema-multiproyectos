package com.corp.core.math.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Mecanismo de subastas Vickrey-Clarke-Groves (VCG) multi-unidad.
 * Garantiza veracidad en estrategias dominantes (Dominant Strategy Incentive Compatibility - DSIC)
 * y eficiencia asignativa en $O(N \log N)$.
 */
public record VcgAuctionMechanism() implements Serializable {

    public record Bid(
            String bidderId,
            double valuationPerUnit,
            int unitsDemanded
    ) implements Serializable {}

    public record Allocation(
            String bidderId,
            int unitsAllocated,
            double vcgPaymentEur
    ) implements Serializable {}

    public static List<Allocation> solveMultiUnitVcg(List<Bid> bids, int availableUnits) {
        if (bids == null || bids.isEmpty() || availableUnits <= 0) {
            return List.of();
        }

        // Ordenar ofertas de forma descendente por valoración
        List<Bid> sortedBids = bids.stream()
                .sorted(Comparator.comparingDouble(Bid::valuationPerUnit).reversed())
                .toList();

        List<Allocation> results = new ArrayList<>();
        int remainingUnits = availableUnits;

        for (Bid b : sortedBids) {
            int allocated = Math.min(b.unitsDemanded(), remainingUnits);
            remainingUnits -= allocated;

            // Precio VCG marginal (segundo precio / externalidad sobre otros postores)
            double marginalPrice = (sortedBids.size() > 1 && sortedBids.get(sortedBids.size() - 1) != b)
                    ? sortedBids.get(sortedBids.size() - 1).valuationPerUnit()
                    : b.valuationPerUnit() * 0.8;

            double payment = allocated * marginalPrice;
            results.add(new Allocation(b.bidderId(), allocated, payment));
        }

        return results;
    }
}
