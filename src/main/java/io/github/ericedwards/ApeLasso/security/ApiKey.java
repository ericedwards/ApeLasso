package io.github.ericedwards.ApeLasso.security;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class ApiKey {

    public static final String ROLE_API_KEY_USER = "ROLE_API_KEY_USER";
    public static final String ROLE_API_KEY_ADMIN = "ROLE_API_KEY_ADMIN";

    @Id
    String name;
    @Lob
    String salt;
    @Lob
    String secret;
    String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
