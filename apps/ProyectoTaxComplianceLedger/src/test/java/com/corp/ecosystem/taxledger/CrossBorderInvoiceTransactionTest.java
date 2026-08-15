package com.corp.ecosystem.taxledger;

import com.corp.ecosystem.taxledger.application.TaxComplianceService;
import com.corp.ecosystem.taxledger.domain.CrossBorderInvoiceTransaction;
import com.corp.ecosystem.taxledger.domain.port.InvoiceLedgerRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoTaxComplianceLedger.
 */
class CrossBorderInvoiceTransactionTest {

    static class InMemoryInvoiceLedgerRepository implements InvoiceLedgerRepositoryPort {
        private final Map<CrossBorderInvoiceTransaction.InvoiceId, CrossBorderInvoiceTransaction> storage = new ConcurrentHashMap<>();

        @Override
        public CrossBorderInvoiceTransaction save(CrossBorderInvoiceTransaction invoice) {
            storage.put(invoice.id(), invoice);
            return invoice;
        }

        @Override
        public Optional<CrossBorderInvoiceTransaction> findById(CrossBorderInvoiceTransaction.InvoiceId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryInvoiceLedgerRepository repository = new InMemoryInvoiceLedgerRepository();
    private final TaxComplianceService service = new TaxComplianceService(repository);

    @Test
    @DisplayName("Debe validar y liquidar factura B2B intracomunitaria conforme a ViDA 2026")
    void shouldClearValidCrossBorderInvoice() {
        CrossBorderInvoiceTransaction invoice = service.processInvoice(
                "siemens-ag-energy",
                "DE123456789",
                "ESA12345678",
                BigDecimal.valueOf(150000.00),
                21.0,
                false
        );

        assertNotNull(invoice.id());
        assertEquals(CrossBorderInvoiceTransaction.TaxClearanceStatus.VIDA_CLEARED_VALID, invoice.clearanceStatus());
        assertEquals(BigDecimal.valueOf(31500.00).stripTrailingZeros(), invoice.amounts().vatAmountEur().stripTrailingZeros());
        assertTrue(invoice.riskAssessment().carouselFraudRiskScore() < 0.10);
    }

    @Test
    @DisplayName("Debe poner en cuarentena factura sospechosa de fraude carrusel / Missing Trader")
    void shouldQuarantineSuspiciousCarouselFraudInvoice() {
        CrossBorderInvoiceTransaction invoice = service.processInvoice(
                "phantom-trader-shell",
                "CY99988877L",
                "ESB88877766",
                BigDecimal.valueOf(5000000.00),
                21.0,
                true // Patrón carrusel detectado
        );

        assertEquals(CrossBorderInvoiceTransaction.TaxClearanceStatus.QUARANTINED_CAROUSEL_FRAUD_SUSPICION, invoice.clearanceStatus());
        assertTrue(invoice.riskAssessment().isMissingTraderAnomalyDetected());
        assertTrue(invoice.riskAssessment().carouselFraudRiskScore() > 0.90);
    }
}
