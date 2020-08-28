package com.damo.shiro.autoconfigure;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.damo.shiro.ShiroFilterDefinition;
import com.damo.shiro.ShiroWebProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * 
 */
public class AbstractShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterConfiguration {
    @Autowired(required = false)
    private ShiroFilterDefinition shiroFilterDefinition;

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filterFactoryBean = super.shiroFilterFactoryBean();
        if (shiroFilterDefinition != null) {
            shiroFilterDefinition.define(filterFactoryBean.getFilters());
        }
        return filterFactoryBean;
    }

    @Bean(name = "filterShiroFilterRegistrationBean")
    @ConditionalOnMissingBean
    protected FilterRegistrationBean filterShiroFilterRegistrationBean() throws Exception {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter((AbstractShiroFilter) shiroFilterFactoryBean().getObject());
        filterRegistrationBean.setOrder(1);

        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(ShiroWebProperties shiroWebProperties) {
        OrderedShiroFilterChainDefinition chainDefinition = new OrderedShiroFilterChainDefinition();
        shiroWebProperties.getFilterChainDefinition().forEach((filter) -> {
            String[] path = filter.split("=");
            chainDefinition.addPathDefinition(path[0], path[1]);
        });
        return chainDefinition;
    }

    private class OrderedShiroFilterChainDefinition implements ShiroFilterChainDefinition {
        final private Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        public void addPathDefinition(String antPath, String definition) {
            filterChainDefinitionMap.put(antPath, definition);
        }

        public void addPathDefinitions(Map<String, String> pathDefinitions) {
            filterChainDefinitionMap.putAll(pathDefinitions);
        }

        @Override
        public Map<String, String> getFilterChainMap() {
            return filterChainDefinitionMap;
        }
    }
}
