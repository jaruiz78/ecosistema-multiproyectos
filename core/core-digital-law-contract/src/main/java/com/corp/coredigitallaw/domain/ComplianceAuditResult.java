package com.corp.coredigitallaw.domain;

import java.io.Serializable;
import java.util.List;

public record ComplianceAuditResult(
        String auditId,
        String regulationFramework,
        boolean compliant,
        List<String> findings
) implements Serializable {}
