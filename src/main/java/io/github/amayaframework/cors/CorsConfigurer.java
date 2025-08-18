package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.http.HttpMethod;

import java.util.Set;

public interface CorsConfigurer extends Resettable {
    // Origins
    Set<String> allowedOrigins(); // get

    CorsConfigurer allowedOrigins(Set<String> origins); // set

    CorsConfigurer allowAnyOrigin();

    CorsConfigurer allowOrigin(String origin);

    CorsConfigurer allowOrigins(String... origins);

    CorsConfigurer allowOrigins(Iterable<String> origins);

    CorsConfigurer denyAnyOrigin();

    CorsConfigurer denyOrigin(String origin);

    CorsConfigurer denyOrigins(String... origins);

    CorsConfigurer denyOrigins(Iterable<String> origins);

    // Methods
    Set<HttpMethod> allowedMethods(); // get

    CorsConfigurer allowedMethods(Set<HttpMethod> methods); // set

    CorsConfigurer allowAnyMethod();

    CorsConfigurer allowMethod(HttpMethod method);

    CorsConfigurer allowMethods(HttpMethod... methods);

    CorsConfigurer allowMethods(Iterable<HttpMethod> methods);

    CorsConfigurer denyAnyMethod();

    CorsConfigurer denyMethod(HttpMethod method);

    CorsConfigurer denyMethods(HttpMethod... methods);

    CorsConfigurer denyMethods(Iterable<HttpMethod> methods);

    // Headers (allowed)
    Set<String> allowedHeaders(); // get

    CorsConfigurer allowedHeaders(Set<String> headers); // set

    CorsConfigurer allowAnyHeader();

    CorsConfigurer allowHeader(String header);

    CorsConfigurer allowHeaders(String... headers);

    CorsConfigurer allowHeaders(Iterable<String> headers);

    CorsConfigurer denyAnyHeader();

    CorsConfigurer denyHeader(String header);

    CorsConfigurer denyHeaders(String... headers);

    CorsConfigurer denyHeaders(Iterable<String> headers);

    // Headers (exposed)
    Set<String> exposedHeaders();

    CorsConfigurer exposedHeaders(Set<String> headers);

    CorsConfigurer exposeAnyHeader();

    CorsConfigurer exposeHeader(String header);

    CorsConfigurer exposeHeaders(String... headers);

    CorsConfigurer exposeHeaders(Iterable<String> headers);

    CorsConfigurer hideAnyHeader();

    CorsConfigurer hideHeader(String header);

    CorsConfigurer hideHeaders(String... headers);

    CorsConfigurer hideHeaders(Iterable<String> headers);

    // Allow any
    CorsConfigurer allowAny();

    // Credentials
    boolean allowCredentials();

    CorsConfigurer allowCredentials(boolean allow);

    // Max-Age
    int maxAge();

    CorsConfigurer maxAge(int seconds);
}
