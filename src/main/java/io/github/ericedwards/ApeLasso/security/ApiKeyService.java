package io.github.ericedwards.ApeLasso.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApiKeyService {

    @Value("${application.security.initialApiKey}")
    private String configApiKey;
    private final ApiKeyRepository apiKeyRepository;
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyService.class);

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String adminName = getName(configApiKey);
        String adminSecret = getSecret(configApiKey);
        ApiKey adminApiKey;
        adminApiKey = apiKeyRepository.findByName(adminName);
        if (adminApiKey == null) {
            logger.info("need to create initial api key");
            ApiKey initialApiKey = new ApiKey();
            initialApiKey.setName(adminName);
            String encodedSalt = SaltedPassword.generateSaltEncoded();
            String encodedSecret = SaltedPassword.encodeHashedPassword(encodedSalt, adminSecret);
            initialApiKey.setSalt(encodedSalt);
            initialApiKey.setSecret(encodedSecret);
            apiKeyRepository.save(initialApiKey);
            logger.info("created initial api key: {}", configApiKey);
        } else {
            logger.info("already have an initial api key");
        }
    }

    public Optional<Authentication> verify(String providedApiKey) {
        if (providedApiKey == null) return Optional.empty();
        String targetName = getName(providedApiKey);
        String targetSecret = getSecret(providedApiKey);
        ApiKey targetApiKey = apiKeyRepository.findByName(targetName);
        if ((targetName != null) && (targetSecret != null)
                && SaltedPassword.check(targetApiKey.getSalt(), targetApiKey.getSecret(), targetSecret)) {
            return Optional.of(new ApiKeyAuth(providedApiKey, AuthorityUtils.NO_AUTHORITIES));
        } else {
            return Optional.empty();
        }
    }

    public String updateApiKey(String targetName) {
        ApiKey targetApiKey;
        targetApiKey = apiKeyRepository.findByName(targetName);
        if (targetApiKey == null) {
            targetApiKey = new ApiKey();
            targetApiKey.setName(targetName);
        }
        String newSecret = SaltedPassword.generateBase64Password();
        String encodedSalt = SaltedPassword.generateSaltEncoded();
        String encodedSecret = SaltedPassword.encodeHashedPassword(encodedSalt, newSecret);
        targetApiKey.setSalt(encodedSalt);
        targetApiKey.setSecret(encodedSecret);
        apiKeyRepository.save(targetApiKey);
        return targetName + ":" + newSecret;
    }

    private String getName(String apiKey) {
        String[] xxx = apiKey.split(":");
        return (xxx.length == 2) ? xxx[0] : null;
    }

    private String getSecret(String apiKey) {
        String[] xxx = apiKey.split(":");
        return (xxx.length == 2) ? xxx[1] : null;
    }

}
