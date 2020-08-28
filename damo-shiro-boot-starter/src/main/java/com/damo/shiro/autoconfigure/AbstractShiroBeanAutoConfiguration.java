package com.damo.shiro.autoconfigure;

import org.apache.shiro.event.EventBus;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.ShiroEventBusBeanPostProcessor;
import org.apache.shiro.spring.config.AbstractShiroBeanConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 
 * 
 */
public class AbstractShiroBeanAutoConfiguration extends AbstractShiroBeanConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @Override
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return super.lifecycleBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public EventBus eventBus() {
        return super.eventBus();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public ShiroEventBusBeanPostProcessor shiroEventBusAwareBeanPostProcessor() {
        return super.shiroEventBusAwareBeanPostProcessor();
    }
}
