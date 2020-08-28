package com.damo.shiro.autoconfigure.stateless.support.jwt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.damo.shiro.autoconfigure.stateless.support.jwt.exception.ErrorFieldException;
import com.damo.shiro.autoconfigure.stateless.support.jwt.exception.MissingFieldsException;

/**
 * 
 * 
 */
public interface PayloadTemplate {
    String getIssuer();

    void addField(String key, Class type);

    boolean hasField(String key, Class type);

    Set<String> getFieldNames();

    Map<String, Class> getFieldMap();

    /**
     * @return missing fields name collection
     */
    List<String> extractMissingFields(Payload payload);

    PayloadChecker check();

    interface PayloadChecker {
        void hasField(String key, Object value) throws ErrorFieldException;

        /**
         * @throws MissingFieldsException when missing fields' size grater than 0
         */
        void missingFields(Payload payload) throws MissingFieldsException;
    }
}
