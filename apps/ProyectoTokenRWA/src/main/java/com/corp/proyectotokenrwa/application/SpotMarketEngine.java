package com.corp.proyectotokenrwa.application;
import com.corp.proyectotokenrwa.domain.asset.TokenizedAsset;
import com.corp.proyectotokenrwa.domain.saga.EscrowTransactionSaga;

public class SpotMarketEngine {
    public EscrowTransactionSaga executeTrade(TokenizedAsset asset, EscrowTransactionSaga saga) {
        if (!asset.isLocked()) {
            throw new IllegalStateException("Asset must be locked in Escrow before trade.");
        }
        return saga.advanceTo("SETTLED");
    }
}
