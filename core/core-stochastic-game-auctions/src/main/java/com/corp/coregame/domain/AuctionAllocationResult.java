package com.corp.coregame.domain;

import java.io.Serializable;
import java.util.List;

public record AuctionAllocationResult(
        String auctionId,
        int totalUnitsAvailable,
        int totalUnitsAllocated,
        double totalRevenueEur,
        boolean strategyProofVerified
) implements Serializable {}
