package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.http.HttpMethod;

/**
 * Builder for {@link CorsConfig} instances.
 * <p>
 * Provides a fluent API to configure allowed origins, methods, headers, exposed headers,
 * credentials, and max age. Builds a {@link CorsConfig} object based on the current state
 * and resets the builder after construction.
 */
public final class CorsConfigBuilder implements CorsConfigurer {
    private CompileOriginBuilder originsBuilder;
    private GenericAccessBuilder<HttpMethod> methodsBuilder;
    private StringAccessBuilder headersBuilder;
    private StringAccessBuilder exposedBuilder;
    private boolean allowCredentials;
    private int maxAge;

    /**
     * Creates a new {@code CorsConfigBuilder} initialized with default values.
     */
    public CorsConfigBuilder() {
        allowCredentials = CorsDefaults.ALLOW_CREDENTIALS;
        maxAge = CorsDefaults.MAX_AGE;
    }

    private static void reset(Resettable resettable) {
        if (resettable != null) {
            resettable.reset();
        }
    }

    /**
     * Resets this builder to its default state.
     * <p>
     * Clears all configured origins, methods, headers, exposed headers,
     * credentials, and max age.
     */
    @Override
    public void reset() {
        reset(originsBuilder);
        reset(methodsBuilder);
        reset(headersBuilder);
        reset(exposedBuilder);
        allowCredentials = CorsDefaults.ALLOW_CREDENTIALS;
        maxAge = CorsDefaults.MAX_AGE;
    }

    @Override
    public OriginConfigurer allowedOrigins() {
        if (originsBuilder == null) {
            originsBuilder = new CompileOriginBuilder();
        }
        return originsBuilder;
    }

    @Override
    public AccessConfigurer<HttpMethod> allowedMethods() {
        if (methodsBuilder == null) {
            methodsBuilder = new GenericAccessBuilder<>();
        }
        return methodsBuilder;
    }

    @Override
    public AccessConfigurer<String> allowedHeaders() {
        if (headersBuilder == null) {
            headersBuilder = new StringAccessBuilder();
        }
        return headersBuilder;
    }

    @Override
    public AccessConfigurer<String> exposedHeaders() {
        if (exposedBuilder == null) {
            exposedBuilder = new StringAccessBuilder();
        }
        return exposedBuilder;
    }

    @Override
    public CorsConfigBuilder allowAny() {
        originsBuilder.allowAny();
        methodsBuilder.allowAny();
        headersBuilder.allowAny();
        exposedBuilder.allowAny();
        return this;
    }

    @Override
    public boolean allowCredentials() {
        return allowCredentials;
    }

    @Override
    public CorsConfigBuilder allowCredentials(boolean allow) {
        allowCredentials = allow;
        return this;
    }

    @Override
    public int maxAge() {
        return maxAge;
    }

    @Override
    public CorsConfigBuilder maxAge(int seconds) {
        maxAge = seconds;
        return this;
    }

    // Private helper methods build individual parts of the config

    private void buildOrigins(CorsConfig config) {
        if (originsBuilder == null) {
            return;
        }
        config.setAllowedOrigins(originsBuilder.buildStrict());
        config.setAllowedRegexes(originsBuilder.buildRegexes());
    }

    private void buildMethods(CorsConfig config) {
        if (methodsBuilder == null) {
            return;
        }
        config.setAllowedMethods(methodsBuilder.build());
    }

    private void buildHeaders(CorsConfig config) {
        if (headersBuilder == null) {
            return;
        }
        config.setAllowedHeaders(headersBuilder.build());
    }

    private void buildExposed(CorsConfig config) {
        if (exposedBuilder == null) {
            return;
        }
        config.setExposedHeaders(exposedBuilder.build());
    }

    /**
     * Builds a {@link CorsConfig} instance using the current configuration.
     * <p>
     * After building, the builder is reset to allow re-use.
     *
     * @return a fully constructed {@link CorsConfig} instance
     */
    public CorsConfig build() {
        try {
            var ret = new CorsConfig();
            buildOrigins(ret);
            buildMethods(ret);
            buildHeaders(ret);
            buildExposed(ret);
            ret.setAllowCredentials(allowCredentials);
            ret.setMaxAge(maxAge);
            return ret;
        } finally {
            reset();
        }
    }
}
