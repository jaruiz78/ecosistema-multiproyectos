package com.corp.proyectotokenrwa.domain.saga;
/**
 * Arquitectura y especificación formal para EscrowTransactionSaga.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public record EscrowTransactionSaga(String transactionId, String state) {
    public EscrowTransactionSaga advanceTo(String newState) {
        return new EscrowTransactionSaga(transactionId, newState);
    }
}
