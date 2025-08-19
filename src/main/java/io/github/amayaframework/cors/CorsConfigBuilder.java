package io.github.amayaframework.cors;

/**
 * Builder for {@link CorsConfig} instances.
 * <p>
 * Provides a fluent API to configure allowed origins, methods, headers, exposed headers,
 * credentials, and max age. Builds a {@link CorsConfig} object based on the current state
 * and resets the builder after construction.
 */
public final class CorsConfigBuilder extends AbstractCorsConfigurer<CorsConfigBuilder> {

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
