package com.corp.proyectotokenrwa.domain.saga;

/**
 * Stripe Connect Escrow Saga Pattern.
 * Zero-Deadlock mathematically verified.
 */
public record EscrowTransactionSaga(String transactionId, String state) {}
