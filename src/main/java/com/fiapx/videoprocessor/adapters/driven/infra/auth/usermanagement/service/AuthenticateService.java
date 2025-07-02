package com.fiapx.videoprocessor.adapters.driven.infra.auth.usermanagement.service;

import com.fiapx.videoprocessor.adapters.driven.infra.auth.usermanagement.mapper.UserInfo2User;
import com.fiapx.videoprocessor.adapters.driven.infra.auth.usermanagement.model.UserInfo;
import com.fiapx.videoprocessor.core.application.exceptions.UnauthorizedException;
import com.fiapx.videoprocessor.core.domain.entities.User;
import com.fiapx.videoprocessor.core.domain.services.auth.IAuhenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticateService implements IAuhenticateService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.user-management.url}")
    public String userManagementUrl;

    @Override
    public User authenticate(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<UserInfo> response = restTemplate.exchange(
                userManagementUrl + "/authenticate",
                HttpMethod.GET,
                request,
                UserInfo.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return UserInfo2User.map(response.getBody());
        } else {
            throw new UnauthorizedException("Invalid token");
        }
    }
}
