package io.github.amayaframework.cors;

/**
 * Provides convenient factory methods for creating CORS configurations
 * and application configurers.
 */
public final class Cors {
    private Cors() {
    }

    /**
     * Creates a new {@link CorsConfigBuilder} pre-configured with default CORS settings.
     * <p>
     * Allows any origin, applies default allowed methods and headers,
     * sets credentials and max-age according to {@link CorsDefaults}.
     *
     * @return a pre-configured {@link CorsConfigBuilder}
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
     * Returns a {@link CorsConfig} instance populated with default values from {@link CorsDefaults}.
     *
     * @return default {@link CorsConfig}
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
     * Creates a new {@link CorsApplicationConfigurer} for a {@link io.github.amayaframework.web.WebApplication}.
     *
     * @param configure if {@code true}, reads {@link CorsOptions} from the application's options
     *                  and applies them automatically
     * @return a new {@link CorsApplicationConfigurer}
     */
    public static CorsApplicationConfigurer applicationConfigurer(boolean configure) {
        return new CorsApplicationConfigurer(configure);
    }

    /**
     * Creates a new {@link CorsApplicationConfigurer} without automatic option configuration,
     * pre-populated with default CORS settings.
     * <p>
     * The returned configurer's {@link CorsConfigurer} is initialized to:
     * <ul>
     *     <li>Allow any origin</li>
     *     <li>Allow default methods from {@link CorsDefaults#ALLOWED_METHODS}</li>
     *     <li>Allow default headers from {@link CorsDefaults#ALLOWED_HEADERS}</li>
     *     <li>Set credentials and max-age from {@link CorsDefaults}</li>
     * </ul>
     *
     * @return a pre-configured {@link CorsApplicationConfigurer}
     */
    public static CorsApplicationConfigurer applicationConfigurer() {
        var ret = applicationConfigurer(false);
        var cfg = ret.getConfigurer();
        cfg.allowedOrigins().allowAny();
        cfg.allowedMethods().allow(CorsDefaults.ALLOWED_METHODS);
        cfg.allowedHeaders().allow(CorsDefaults.ALLOWED_HEADERS);
        cfg.allowCredentials(CorsDefaults.ALLOW_CREDENTIALS).maxAge(CorsDefaults.MAX_AGE);
        return ret;
    }
}
