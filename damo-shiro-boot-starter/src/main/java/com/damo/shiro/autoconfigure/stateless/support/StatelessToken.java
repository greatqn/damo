package com.damo.shiro.autoconfigure.stateless.support;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 
 * 
 */
public class StatelessToken implements AuthenticationToken {
    private String token;

    public StatelessToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
