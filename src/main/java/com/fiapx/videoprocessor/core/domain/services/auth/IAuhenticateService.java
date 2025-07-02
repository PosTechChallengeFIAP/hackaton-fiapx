package com.fiapx.videoprocessor.core.domain.services.auth;

import com.fiapx.videoprocessor.core.domain.entities.User;

public interface IAuhenticateService {
    User authenticate(String token);
}
