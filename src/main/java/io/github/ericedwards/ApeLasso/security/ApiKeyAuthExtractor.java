package io.github.ericedwards.ApeLasso.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApiKeyAuthExtractor {

    private String apiKey = "aloha";

    public Optional<Authentication> extract(HttpServletRequest request) {
        String providedKey = request.getHeader("X-API-KEY");
        if (providedKey == null || !providedKey.equals(apiKey))
            return Optional.empty();
        return Optional.of(new ApiKeyAuth(providedKey, AuthorityUtils.NO_AUTHORITIES));
    }

}
