package com.corp.ecosystem.taxledger.application;

import com.corp.ecosystem.taxledger.domain.CrossBorderInvoiceTransaction;
import com.corp.ecosystem.taxledger.domain.port.InvoiceLedgerRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaxComplianceService {

    private final InvoiceLedgerRepositoryPort repositoryPort;

    public TaxComplianceService(InvoiceLedgerRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public CrossBorderInvoiceTransaction processInvoice(
            String tenantId,
            String issuerVat,
            String recipientVat,
            BigDecimal baseAmountEur,
            double vatRatePct,
            boolean hasCarouselFraudPattern
    ) {
        CrossBorderInvoiceTransaction.InvoiceId id = new CrossBorderInvoiceTransaction.InvoiceId("VIDA-INV-" + System.nanoTime());
        CrossBorderInvoiceTransaction invoice = CrossBorderInvoiceTransaction.clearInvoice(
                id, tenantId, issuerVat, recipientVat, baseAmountEur, vatRatePct, hasCarouselFraudPattern
        );
        return repositoryPort.save(invoice);
    }

    public Optional<CrossBorderInvoiceTransaction> getInvoice(CrossBorderInvoiceTransaction.InvoiceId id) {
        return repositoryPort.findById(id);
    }
}
