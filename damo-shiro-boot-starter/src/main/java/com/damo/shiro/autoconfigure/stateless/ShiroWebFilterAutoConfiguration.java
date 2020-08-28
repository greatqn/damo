package com.damo.shiro.autoconfigure.stateless;

import com.damo.shiro.ShiroFilterDefinition;
import com.damo.shiro.ShiroWebProperties;
import com.damo.shiro.autoconfigure.AbstractShiroWebFilterAutoConfiguration;
import com.damo.shiro.autoconfigure.stateless.support.StatelessUserFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * 
 */
@Configuration
@ConditionalOnProperty(name = "shiro.web.mode", havingValue = "STATELESS")
public class ShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(ShiroWebFilterAutoConfiguration.class);
    @Autowired
    private ShiroWebProperties shiroWebProperties;

    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterDefinition shiroFilterDefinition() {
        return filters -> {
            logger.info("replace [authc] filter by " + StatelessUserFilter.class);
            filters.put("authc", new StatelessUserFilter(shiroWebProperties));
        };
    }
}
