package com.https756.resourceservice.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class KeycloakJwtDecoder {

    @Bean
    public JwtDecoder decode(JwtProperties jwtProperties) {
        return NimbusJwtDecoder.withIssuerLocation(jwtProperties.issuerUri()).build();
    }

}
