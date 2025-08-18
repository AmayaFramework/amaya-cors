package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.http.HttpMethod;

public interface CorsConfigurer extends Resettable {

    OriginConfigurer allowedOrigins();

    AccessConfigurer<HttpMethod> allowedMethods();

    AccessConfigurer<String> allowedHeaders();

    AccessConfigurer<String> exposedHeaders();

    // Allow any
    CorsConfigurer allowAny();

    // Credentials
    boolean allowCredentials();

    CorsConfigurer allowCredentials(boolean allow);

    // Max-Age
    int maxAge();

    CorsConfigurer maxAge(int seconds);
}
