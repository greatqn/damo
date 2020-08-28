package com.damo.shiro.autoconfigure.stateless.support.hash;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import com.damo.shiro.autoconfigure.stateless.support.StatelessToken;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 */
public class SimpleHashRealm extends AuthorizingRealm {
    private static Map<String, SimpleAccount> map = new HashMap<>();

    public SimpleHashRealm() {
    }

    public SimpleHashRealm(String name) {
        super();
        super.setName(name);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    public void addAccount(String hash, String... roles) {
        map.put(hash, new SimpleAccount(hash, null, this.getName(), CollectionUtils.asSet(roles), null));
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        StatelessToken token = (StatelessToken) authenticationToken;
        SimpleAccount account = map.get(token.getPrincipal());
        if (account != null) {
            if (account.isLocked()) {
                throw new LockedAccountException("Account [" + account + "] is locked.");
            }

            if (account.isCredentialsExpired()) {
                String msg = "The credentials for account [" + account + "] are expired";
                throw new ExpiredCredentialsException(msg);
            }
        }
        return account;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAccount account = map.get(principalCollection.getPrimaryPrincipal());
        return account;
    }
}

