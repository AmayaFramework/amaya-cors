package io.github.amayaframework.cors;

import com.github.romanqed.jtype.JType;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.options.Key;

/**
 * Defines constants and keys for configuring CORS in a {@link io.github.amayaframework.web.WebApplication}.
 * <p>
 * Includes option names for enabling credentials, allowing any origin/method/header,
 * setting allowed origins, methods, headers, exposed headers, and max age.
 */
public final class CorsOptions {
    private CorsOptions() {
    }

    /**
     * Name of the configuration group for CORS options.
     */
    public static final String CORS_GROUP = "cors";

    /**
     * Option to allow any origin, method, and header.
     */
    public static final String ALLOW_ANY = "allow_any";

    /**
     * Option to allow any origin.
     */
    public static final String ALLOW_ANY_ORIGIN = "allow_any_origin";

    /**
     * Option to allow any HTTP method.
     */
    public static final String ALLOW_ANY_METHOD = "allow_any_method";

    /**
     * Option to allow any request header.
     */
    public static final String ALLOW_ANY_HEADER = "allow_any_header";

    /**
     * Option to expose all response headers.
     */
    public static final String EXPOSE_ANY_HEADER = "expose_any_header";

    /**
     * Option to allow credentials in CORS requests.
     */
    public static final String ALLOW_CREDENTIALS = "allow_credentials";

    /**
     * Option key for the maximum age (in seconds) of preflight cache.
     */
    public static final Key<Integer> MAX_AGE = Key.of("max_age", Integer.class);

    /**
     * Option key for explicitly allowed origins.
     */
    public static final Key<Iterable<String>> ALLOWED_ORIGINS = Key.of("allowed_origins", new JType<>(){});

    /**
     * Option key for regex patterns defining allowed origins.
     */
    public static final Key<Iterable<String>> ORIGIN_REGEXES = Key.of("origin_regexes", new JType<>(){});

    /**
     * Option key for explicitly allowed HTTP methods.
     */
    public static final Key<Iterable<HttpMethod>> ALLOWED_METHODS = Key.of("allowed_methods", new JType<>(){});

    /**
     * Option key for explicitly allowed request headers.
     */
    public static final Key<Iterable<String>> ALLOWED_HEADERS = Key.of("allowed_headers", new JType<>(){});

    /**
     * Option key for headers to be exposed in CORS responses.
     */
    public static final Key<Iterable<String>> EXPOSED_HEADERS = Key.of("exposed_headers", new JType<>(){});
}
