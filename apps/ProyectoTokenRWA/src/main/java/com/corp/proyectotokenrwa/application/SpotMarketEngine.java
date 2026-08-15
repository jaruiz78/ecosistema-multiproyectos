package com.corp.proyectotokenrwa.application;

import com.corp.proyectotokenrwa.domain.asset.TokenizedAsset;
import com.corp.proyectotokenrwa.domain.saga.EscrowTransactionSaga;

public class SpotMarketEngine {
    public EscrowTransactionSaga executeTrade(TokenizedAsset asset, EscrowTransactionSaga saga) {
        if (asset.value().compareTo(java.math.BigDecimal.ZERO) > 0) {
            return new EscrowTransactionSaga(saga.transactionId(), "SETTLED");
        }
        return saga;
    }
}
