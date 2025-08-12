package io.github.ericedwards.ApeLasso.security;

import org.springframework.data.repository.CrudRepository;

public interface ApiKeyRepository extends CrudRepository<ApiKey, String> {

    ApiKey findByName(String name);

}