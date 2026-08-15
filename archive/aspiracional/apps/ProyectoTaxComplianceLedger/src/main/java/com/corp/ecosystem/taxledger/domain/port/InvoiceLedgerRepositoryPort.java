package com.corp.ecosystem.taxledger.domain.port;

import com.corp.ecosystem.taxledger.domain.CrossBorderInvoiceTransaction;
import java.util.Optional;

public interface InvoiceLedgerRepositoryPort {
    CrossBorderInvoiceTransaction save(CrossBorderInvoiceTransaction invoice);
    Optional<CrossBorderInvoiceTransaction> findById(CrossBorderInvoiceTransaction.InvoiceId id);
}
