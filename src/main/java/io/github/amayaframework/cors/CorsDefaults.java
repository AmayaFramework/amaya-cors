package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

import java.util.Set;

/**
 * Provides default values for CORS configuration, such as allowed origins,
 * methods, headers, credential policy, and max-age.
 */
public final class CorsDefaults {
    private CorsDefaults() {
    }

    /**
     * Default allowed origins; {@code null} means any origin is allowed.
     */
    public static final Set<String> ALLOWED_ORIGINS = null;

    /**
     * Default allowed HTTP methods.
     */
    public static final Set<HttpMethod> ALLOWED_METHODS = Set.of(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST);

    /**
     * Default allowed request headers.
     */
    public static final Set<String> ALLOWED_HEADERS = Set.of(
            "Accept",
            "Accept-Language",
            "Content-Language",
            "Content-Type",
            "Range"
    );

    /**
     * Default credentials policy; {@code false} means credentials are not allowed.
     */
    public static final boolean ALLOW_CREDENTIALS = false;


    /**
     * Default max-age in seconds for preflight requests; {@code -1} means not set.
     */
    public static final int MAX_AGE = -1;
}
