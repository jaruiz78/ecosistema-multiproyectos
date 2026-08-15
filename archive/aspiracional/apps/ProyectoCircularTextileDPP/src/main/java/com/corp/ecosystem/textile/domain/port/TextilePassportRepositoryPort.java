package com.corp.ecosystem.textile.domain.port;

import com.corp.ecosystem.textile.domain.TextileProductPassport;
import java.util.Optional;

public interface TextilePassportRepositoryPort {
    TextileProductPassport save(TextileProductPassport passport);
    Optional<TextileProductPassport> findById(TextileProductPassport.PassportId id);
}
