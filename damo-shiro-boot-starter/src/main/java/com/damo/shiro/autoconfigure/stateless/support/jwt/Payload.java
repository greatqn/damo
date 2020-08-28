package com.damo.shiro.autoconfigure.stateless.support.jwt;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 */
public class Payload extends HashMap<String, Object> {
    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        super.putAll(m);
    }
}
