package com.corp.ecosystem.intermodal.domain.port;

import com.corp.ecosystem.intermodal.domain.IntermodalTransferHub;
import java.util.Optional;

public interface IntermodalHubRepositoryPort {
    IntermodalTransferHub save(IntermodalTransferHub hub);
    Optional<IntermodalTransferHub> findById(IntermodalTransferHub.HubId id);
}
