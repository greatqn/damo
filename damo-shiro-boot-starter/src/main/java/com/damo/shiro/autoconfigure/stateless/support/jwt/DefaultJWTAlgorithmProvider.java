package com.damo.shiro.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.damo.shiro.ShiroWebProperties;

import java.util.UUID;

/**
 * 
 * 
 */
public class DefaultJWTAlgorithmProvider implements JWTAlgorithmProvider {
    private static String SECRET = UUID.randomUUID().toString();
    private static Algorithm algorithm;

    private ShiroWebProperties shiroWebProperties;

    public DefaultJWTAlgorithmProvider(ShiroWebProperties shiroWebProperties) {
        this.shiroWebProperties = shiroWebProperties;
        if(!shiroWebProperties.getSecret().equals("uuid")){
            SECRET = shiroWebProperties.getSecret();
        }
    }
    
    @Override
    public Algorithm get() {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(SECRET);
        }
        return algorithm;
    }
}
