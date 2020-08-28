package com.damo.shiro.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.algorithms.Algorithm;

import java.util.function.Supplier;

/**
 * 
 * 
 */
public interface JWTAlgorithmProvider extends Supplier<Algorithm> {
}
