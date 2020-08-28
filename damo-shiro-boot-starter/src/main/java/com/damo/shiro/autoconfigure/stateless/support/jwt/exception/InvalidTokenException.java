package com.damo.shiro.autoconfigure.stateless.support.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * 
 */
public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String token) {
        super(String.format("invalid token: %s", token));
    }
}
