package com.damo.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * 
 */
@ConfigurationProperties("shiro.web")
public class ShiroWebProperties {
    private ShiroMode mode = ShiroMode.SESSION;
    private Boolean isRedirectEnabled = true;
    private String tokenName = "token";
    private String secret = "uuid";
    private List<String> filterChainDefinition = new ArrayList<>();

    public ShiroMode getMode() {
        return mode;
    }

    public void setMode(ShiroMode mode) {
        this.mode = mode;
    }

    public Boolean getRedirectEnabled() {
        return isRedirectEnabled;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public void setRedirectEnabled(Boolean redirectEnabled) {
        isRedirectEnabled = redirectEnabled;
    }

    public List<String> getFilterChainDefinition() {
        return filterChainDefinition;
    }

    public void setFilterChainDefinition(List<String> filterChainDefinition) {
        this.filterChainDefinition = filterChainDefinition;
    }

}
