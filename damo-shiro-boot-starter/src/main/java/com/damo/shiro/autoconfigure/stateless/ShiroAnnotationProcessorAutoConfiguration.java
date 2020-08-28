package com.damo.shiro.autoconfigure.stateless;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.damo.shiro.autoconfigure.AbstractShiroAnnotationProcessorAutoConfiguration;

/**
 * 
 * 
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@ConditionalOnProperty(name = "shiro.web.mode", havingValue = "STATELESS")
public class ShiroAnnotationProcessorAutoConfiguration extends AbstractShiroAnnotationProcessorAutoConfiguration {
}
