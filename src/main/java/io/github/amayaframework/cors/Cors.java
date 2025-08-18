package io.github.amayaframework.cors;

/**
 *
 */
public final class Cors {
    private Cors() {
    }

    /**
     *
     * @return
     */
    public static CorsConfigBuilder configBuilder() {
        var ret = new CorsConfigBuilder();
        ret.allowedOrigins().allowAny();
        ret.allowedMethods().allow(CorsDefaults.ALLOWED_METHODS);
        ret.allowedHeaders().allow(CorsDefaults.ALLOWED_HEADERS);
        ret.allowCredentials(CorsDefaults.ALLOW_CREDENTIALS).maxAge(CorsDefaults.MAX_AGE);
        return ret;
    }

    /**
     *
     * @return
     */
    public static CorsConfig defaultConfig() {
        var ret = new CorsConfig();
        ret.setAllowedOrigins(CorsDefaults.ALLOWED_ORIGINS);
        ret.setAllowedMethods(CorsDefaults.ALLOWED_METHODS);
        ret.setAllowedHeaders(CorsDefaults.ALLOWED_HEADERS);
        ret.setAllowCredentials(CorsDefaults.ALLOW_CREDENTIALS);
        ret.setMaxAge(CorsDefaults.MAX_AGE);
        return ret;
    }

    /**
     *
     * @param configure
     * @return
     */
    public static CorsApplicationConfigurer applicationConfigurer(boolean configure) {
        return new CorsApplicationConfigurer(configure);
    }

    /**
     *
     * @return
     */
    public static CorsApplicationConfigurer applicationConfigurer() {
        return applicationConfigurer(false);
    }
}
