package com.corp.proyectotokenrwa.domain.saga;
public record EscrowTransactionSaga(String transactionId, String state) {
    public EscrowTransactionSaga advanceTo(String newState) {
        return new EscrowTransactionSaga(transactionId, newState);
    }
}
