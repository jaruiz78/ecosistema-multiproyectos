package com.corp.ecosystem.zerotrustot.domain.port;

import com.corp.ecosystem.zerotrustot.domain.ScadaNodeSecurityTwin;
import java.util.Optional;

public interface ScadaSecurityRepositoryPort {
    ScadaNodeSecurityTwin save(ScadaNodeSecurityTwin node);
    Optional<ScadaNodeSecurityTwin> findById(ScadaNodeSecurityTwin.NodeSecurityId id);
}
