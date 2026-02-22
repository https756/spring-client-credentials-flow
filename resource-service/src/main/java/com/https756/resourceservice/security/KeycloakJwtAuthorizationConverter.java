package com.https756.resourceservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeycloakJwtAuthorizationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt token) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        List<String> scope = token.getClaimAsStringList("scope");
        List<String> roles = token.getClaimAsStringList("roles");

        for (String s : scope) {
            authorities.add(new SimpleGrantedAuthority(s));
        }

        for (String r : roles) {
            authorities.add(new SimpleGrantedAuthority(r));
        }

        return authorities;
    }

}
