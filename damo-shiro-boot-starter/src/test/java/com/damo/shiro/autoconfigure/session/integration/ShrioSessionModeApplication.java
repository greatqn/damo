package com.damo.shiro.autoconfigure.session.integration;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 
 * 
 */
@SpringBootApplication
public class ShrioSessionModeApplication {
    @Bean
    public Realm realm() {
        SimpleAccountRealm realm = new SimpleAccountRealm("test_realm");
        realm.addAccount("tac", "123456", "staff");
        return realm;
    }
}
