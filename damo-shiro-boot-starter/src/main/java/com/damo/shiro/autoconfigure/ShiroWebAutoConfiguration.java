package com.damo.shiro.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.damo.shiro.ShiroWebProperties;

/**
 * 
 * 
 */
@Configuration
@EnableConfigurationProperties(ShiroWebProperties.class)
public class ShiroWebAutoConfiguration {
}
