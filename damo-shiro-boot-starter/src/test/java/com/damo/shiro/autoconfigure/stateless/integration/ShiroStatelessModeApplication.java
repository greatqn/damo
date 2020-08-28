package com.damo.shiro.autoconfigure.stateless.integration;

import com.damo.shiro.autoconfigure.stateless.support.StatelessCredentialsMatcher;
import com.damo.shiro.autoconfigure.stateless.support.hash.SimpleHashRealm;

import org.apache.shiro.realm.Realm;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 
 * 
 */
@SpringBootApplication
public class ShiroStatelessModeApplication {
    @Bean
    public Realm realm() {
        SimpleHashRealm realm = new SimpleHashRealm();
        realm.setCredentialsMatcher(new StatelessCredentialsMatcher());
        realm.addAccount("cd6765734a16476b9bd4b0513b3fb8e4", "staff");
        return realm;
    }
}
