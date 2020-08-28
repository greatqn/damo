package com.damo.shiro.autoconfigure.stateless.support;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;

/**
 * 
 * 
 */
public class StatelessSessionStorageEvaluator extends DefaultSessionStorageEvaluator {
    public StatelessSessionStorageEvaluator() {
        super();
        setSessionStorageEnabled(false);
    }
}
