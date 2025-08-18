package io.github.amayaframework.cors;

public final class CorsConfigBuilder extends AbstractCorsConfigurer<CorsConfigBuilder> {
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
