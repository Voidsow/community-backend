package com.voidsow.community.backend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class Container {
    @Bean
    public Key key() {return Keys.secretKeyFor(SignatureAlgorithm.HS256);}
}
