package com.damo.shiro;

import javax.servlet.Filter;
import java.util.Map;

/**
 * 
 * 
 */
public interface ShiroFilterDefinition {
    void define(Map<String, Filter> filters);
}
