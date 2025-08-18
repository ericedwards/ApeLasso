package io.github.ericedwards.ApeLasso;

import io.github.ericedwards.ApeLasso.security.ApiKeyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @SecurityRequirement(name = "API Key Authentication")
    @PreAuthorize("hasRole('ROLE_API_KEY_ADMIN')")
    @PostMapping("/protected/apiKey")
    public ResponseEntity<String> updateApiKey(@RequestParam String apiKeyName) {
        return ResponseEntity.ok(apiKeyService.updateApiKey(apiKeyName));
    }

}
