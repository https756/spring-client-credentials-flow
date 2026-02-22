package com.https756.resourceservice.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt")
public record JwtProperties(
        String issuerUri
) {
}
