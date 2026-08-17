package com.corp.coregame.application;

import com.corp.core.math.game.VcgAuctionMechanism;
import com.corp.coregame.domain.AuctionAllocationResult;

import java.util.List;

public class MultiUnitResourceAuctionUseCase {

    public AuctionAllocationResult runVcgAuction(String auctionId, List<VcgAuctionMechanism.Bid> bids, int units) {
        List<VcgAuctionMechanism.Allocation> allocations = VcgAuctionMechanism.solveMultiUnitVcg(bids, units);

        int totalAllocated = allocations.stream().mapToInt(VcgAuctionMechanism.Allocation::unitsAllocated).sum();
        double totalRevenue = allocations.stream().mapToDouble(VcgAuctionMechanism.Allocation::vcgPaymentEur).sum();

        return new AuctionAllocationResult(auctionId, units, totalAllocated, totalRevenue, true);
    }
}
