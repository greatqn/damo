package com.damo.shiro.autoconfigure.stateless.support.jwt;

/**
 * todo:: unit test
 *
 * 
 * 
 */
public class SimpleJWTRealm extends AbstractJWTRealm {
    public SimpleJWTRealm(String issuer, JWTManager jwtManager) {
        super(issuer, jwtManager);
    }
}
