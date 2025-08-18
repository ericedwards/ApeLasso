package io.github.ericedwards.ApeLasso.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApiKeyAuthExtractor {

    private final ApiKeyService apiKeyService;

    public ApiKeyAuthExtractor(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    public Optional<Authentication> extract(HttpServletRequest request) {
        String providedKey = request.getHeader("X-API-KEY");
        return apiKeyService.verify(providedKey);
    }

}
