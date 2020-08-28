package com.damo.shiro.autoconfigure.stateless.support.jwt;

import org.junit.Test;

import com.damo.shiro.autoconfigure.stateless.support.jwt.SimpleJWTRealm;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 
 * 
 */
public class SimpleJWTRealmTest {
    @Test
    public void split() throws Exception {
        assertThat(SimpleJWTRealm.split("user,staff,baby")).contains("user", "staff", "baby");
    }
}