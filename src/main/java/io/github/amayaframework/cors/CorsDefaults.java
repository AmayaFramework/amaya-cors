package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

import java.util.Set;

/**
 *
 */
public final class CorsDefaults {
    private CorsDefaults() {
    }

    /**
     *
     */
    public static final Set<String> ALLOWED_ORIGINS = null;

    /**
     *
     */
    public static final Set<HttpMethod> ALLOWED_METHODS = Set.of(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST);

    /**
     *
     */
    public static final Set<String> ALLOWED_HEADERS = Set.of(
            "Accept",
            "Accept-Language",
            "Content-Language",
            "Content-Type",
            "Range"
    );

    /**
     *
     */
    public static final boolean ALLOW_CREDENTIALS = false;

    /**
     *
     */
    public static final int MAX_AGE = -1;
}
