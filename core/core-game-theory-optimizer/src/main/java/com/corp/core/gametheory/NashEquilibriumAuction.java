package com.corp.core.gametheory;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Modelo Analítico: NashEquilibriumAuction (Optimización de Subastas Dobles y Equilibrios de Nash).
 */
public record NashEquilibriumAuction(
        String auctionId,
        List<Bidder> bidders,
        List<AskOffer> asks,
        ClearingResult clearingResult
) implements Serializable {

    public record Bidder(String bidderId, double maxPriceEur, double quantity) {}
    public record AskOffer(String sellerId, double minPriceEur, double quantity) {}

    public record ClearingResult(
            double clearingPriceEur,
            double totalVolumeMatched,
            boolean isNashEquilibriumStable
    ) {}

    public static NashEquilibriumAuction computeEquilibrium(String auctionId, List<Bidder> bids, List<AskOffer> asks) {
        Objects.requireNonNull(auctionId, "auctionId no puede ser nulo");
        Objects.requireNonNull(bids, "bids no puede ser nulo");
        Objects.requireNonNull(asks, "asks no puede ser nulo");

        double avgBid = bids.stream().mapToDouble(Bidder::maxPriceEur).average().orElse(0.0);
        double avgAsk = asks.stream().mapToDouble(AskOffer::minPriceEur).average().orElse(0.0);
        double clearingPrice = (avgBid + avgAsk) / 2.0;

        double totalBidQty = bids.stream().mapToDouble(Bidder::quantity).sum();
        double totalAskQty = asks.stream().mapToDouble(AskOffer::quantity).sum();
        double volumeMatched = Math.min(totalBidQty, totalAskQty);

        boolean isStable = avgBid >= avgAsk && volumeMatched > 0.0;
        ClearingResult result = new ClearingResult(clearingPrice, volumeMatched, isStable);

        return new NashEquilibriumAuction(auctionId, bids, asks, result);
    }
}
