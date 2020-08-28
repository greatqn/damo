package com.damo.shiro.autoconfigure.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.damo.shiro.autoconfigure.AbstractShiroWebFilterAutoConfiguration;

/**
 * 
 * 
 */
@Configuration
@ConditionalOnProperty(name = "shiro.web.mode", havingValue = "SESSION", matchIfMissing = true)
public class ShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterAutoConfiguration {
}
