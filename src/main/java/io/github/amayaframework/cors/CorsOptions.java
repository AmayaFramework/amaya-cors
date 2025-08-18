package io.github.amayaframework.cors;

import com.github.romanqed.jtype.JType;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.options.Key;

/**
 *
 */
public final class CorsOptions {
    private CorsOptions() {
    }

    /**
     *
     */
    public static final String CORS_GROUP = "cors";

    /**
     *
     */
    public static final String ALLOW_ANY = "allow_any";

    /**
     *
     */
    public static final String ALLOW_ANY_ORIGIN = "allow_any_origin";

    /**
     *
     */
    public static final String ALLOW_ANY_METHOD = "allow_any_method";

    /**
     *
     */
    public static final String ALLOW_ANY_HEADER = "allow_any_header";

    /**
     *
     */
    public static final String EXPOSE_ANY_HEADER = "expose_any_header";

    /**
     *
     */
    public static final String ALLOW_CREDENTIALS = "allow_credentials";

    /**
     *
     */
    public static final Key<Integer> MAX_AGE = Key.of("max_age", Integer.class);

    /**
     *
     */
    public static final Key<Iterable<String>> ALLOWED_ORIGINS = Key.of("allowed_origins", new JType<>(){});

    /**
     *
     */
    public static final Key<Iterable<String>> ORIGIN_REGEXES = Key.of("origin_regexes", new JType<>(){});

    /**
     *
     */
    public static final Key<Iterable<HttpMethod>> ALLOWED_METHODS = Key.of("allowed_methods", new JType<>(){});

    /**
     *
     */
    public static final Key<Iterable<String>> ALLOWED_HEADERS = Key.of("allowed_headers", new JType<>(){});

    /**
     *
     */
    public static final Key<Iterable<String>> EXPOSED_HEADERS = Key.of("exposed_headers", new JType<>(){});
}
