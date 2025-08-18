package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.http.HttpMethod;

/**
 *
 */
public interface CorsConfigurer extends Resettable {

    /**
     *
     * @return
     */
    OriginConfigurer allowedOrigins();

    /**
     *
     * @return
     */
    AccessConfigurer<HttpMethod> allowedMethods();

    /**
     *
     * @return
     */
    AccessConfigurer<String> allowedHeaders();

    /**
     *
     * @return
     */
    AccessConfigurer<String> exposedHeaders();

    /**
     *
     * @return
     */
    CorsConfigurer allowAny();

    /**
     *
     * @return
     */
    boolean allowCredentials();

    /**
     *
     * @param allow
     * @return
     */
    CorsConfigurer allowCredentials(boolean allow);

    /**
     *
     * @return
     */
    int maxAge();

    /**
     *
     * @param seconds
     * @return
     */
    CorsConfigurer maxAge(int seconds);
}
