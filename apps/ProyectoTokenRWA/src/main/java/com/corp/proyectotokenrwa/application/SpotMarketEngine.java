package com.corp.proyectotokenrwa.application;
import com.corp.proyectotokenrwa.domain.asset.TokenizedAsset;
import com.corp.proyectotokenrwa.domain.saga.EscrowTransactionSaga;

/**
 * Arquitectura y especificación formal para SpotMarketEngine.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public class SpotMarketEngine {
    public EscrowTransactionSaga executeTrade(TokenizedAsset asset, EscrowTransactionSaga saga) {
        if (!asset.isLocked()) {
            throw new IllegalStateException("Asset must be locked in Escrow before trade.");
        }
        return saga.advanceTo("SETTLED");
    }
}
