package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.http.HttpMethod;

/**
 * Central configuration interface for CORS.
 * <p>
 * Provides builders for origins, methods, headers, and other policies
 * defined by the CORS specification.
 */
public interface CorsConfigurer extends Resettable {

    /**
     * Configures allowed origins.
     *
     * @return an {@link OriginConfigurer} instance
     */
    OriginConfigurer allowedOrigins();

    /**
     * Configures allowed HTTP methods.
     *
     * @return an {@link AccessConfigurer} for {@link HttpMethod}
     */
    AccessConfigurer<HttpMethod> allowedMethods();

    /**
     * Configures allowed request headers.
     *
     * @return an {@link AccessConfigurer} for header names
     */
    AccessConfigurer<String> allowedHeaders();

    /**
     * Configures exposed response headers.
     *
     * @return an {@link AccessConfigurer} for header names
     */
    AccessConfigurer<String> exposedHeaders();

    /**
     * Allows any origin, method, and header.
     *
     * @return this configurer for chaining
     */
    CorsConfigurer allowAny();

    /**
     * Returns whether credentials are allowed.
     *
     * @return {@code true} if credentials are allowed
     */
    boolean allowCredentials();

    /**
     * Sets whether credentials are allowed.
     *
     * @param allow {@code true} to allow credentials
     * @return this configurer for chaining
     */
    CorsConfigurer allowCredentials(boolean allow);

    /**
     * Returns the configured max age for preflight caching.
     *
     * @return max age in seconds
     */
    int maxAge();

    /**
     * Sets the max age for preflight caching.
     *
     * @param seconds duration in seconds
     * @return this configurer for chaining
     */
    CorsConfigurer maxAge(int seconds);
}
