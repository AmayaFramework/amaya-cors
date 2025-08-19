package io.github.amayaframework.cors;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.options.OptionSet;
import io.github.amayaframework.web.WebApplication;

/**
 * Configures CORS for a {@link WebApplication} using {@link CorsConfigBuilder} and {@link CorsTask}.
 * <p>
 * Reads options from {@link CorsOptions} group if {@code configure} is {@code true}.
 * Builds a {@link CorsConfig} and registers a {@link CorsTask} to the application.
 */
public final class CorsApplicationConfigurer implements Runnable1<WebApplication> {
    private final CorsConfigBuilder builder;
    private final boolean configure;
    private HttpMethodParser parser;
    private Iterable<HttpMethod> allMethods;

    /**
     * Creates a new configurer.
     *
     * @param configure if {@code true}, applies CORS options from the application's option set
     */
    public CorsApplicationConfigurer(boolean configure) {
        this.builder = new CorsConfigBuilder();
        this.configure = configure;
    }

    /**
     * Creates a new configurer without automatic option configuration.
     */
    public CorsApplicationConfigurer() {
        this(false);
    }

    /**
     * Returns the underlying {@link CorsConfigurer} used to build the configuration.
     *
     * @return the builder implementing {@link CorsConfigurer}
     */
    public CorsConfigurer getConfigurer() {
        return builder;
    }

    /**
     * Returns the parser used to convert strings to {@link HttpMethod} values
     * when handling preflight requests.
     *
     * @return the HTTP method parser
     */
    public HttpMethodParser getParser() {
        return parser;
    }

    /**
     * Sets the parser used to convert strings to {@link HttpMethod} values
     * for preflight requests.
     *
     * @param parser the HTTP method parser to use
     */
    public void setParser(HttpMethodParser parser) {
        this.parser = parser;
    }

    /**
     * Returns all available HTTP methods used when {@code allow any method} is enabled.
     *
     * @return iterable of all HTTP methods
     */
    public Iterable<HttpMethod> getAllMethods() {
        return allMethods;
    }

    /**
     * Sets the iterable of all HTTP methods to use when {@code allow any method} is enabled.
     *
     * @param allMethods iterable of HTTP methods
     */
    public void setAllMethods(Iterable<HttpMethod> allMethods) {
        this.allMethods = allMethods;
    }

    /**
     * Reads {@link CorsOptions} from the given {@link OptionSet} and applies them
     * to the internal {@link CorsConfigBuilder}.
     *
     * @param options the option set containing CORS configuration
     */
    public void configure(OptionSet options) {
        if (options.asKey(CorsOptions.ALLOW_CREDENTIALS)) {
            builder.allowCredentials(true);
        }
        var maxAge = options.get(CorsOptions.MAX_AGE);
        if (maxAge != null) {
            builder.maxAge(maxAge);
        }
        if (options.asKey(CorsOptions.ALLOW_ANY)) {
            builder.allowAny();
            return;
        }
        if (options.asKey(CorsOptions.ALLOW_ANY_ORIGIN)) {
            builder.allowedOrigins().allowAny();
        } else {
            var allowedOrigins = builder.allowedOrigins();
            allowedOrigins.allow(options.get(CorsOptions.ALLOWED_ORIGINS));
            var regexes = options.get(CorsOptions.ORIGIN_REGEXES);
            if (regexes != null) {
                for (var regex : regexes) {
                    if (regex == null) {
                        continue;
                    }
                    allowedOrigins.addRegex(regex);
                }
            }
        }
        if (options.asKey(CorsOptions.ALLOW_ANY_METHOD)) {
            builder.allowedMethods().allowAny();
        } else {
            builder.allowedMethods().allow(options.get(CorsOptions.ALLOWED_METHODS));
        }
        if (options.asKey(CorsOptions.ALLOW_ANY_HEADER)) {
            builder.allowedHeaders().allowAny();
        } else {
            builder.allowedHeaders().allow(options.get(CorsOptions.ALLOWED_HEADERS));
        }
        if (options.asKey(CorsOptions.EXPOSE_ANY_HEADER)) {
            builder.exposedHeaders().allowAny();
        } else {
            builder.exposedHeaders().allow(options.get(CorsOptions.EXPOSED_HEADERS));
        }
    }

    /**
     * Applies the configured CORS settings to the given {@link WebApplication}.
     * <p>
     * If {@code configure} is {@code true}, reads {@link CorsOptions} from the application
     * options and applies them to the builder. Then builds a {@link CorsConfig} and registers
     * a {@link CorsTask} for handling CORS requests.
     *
     * @param app the web application to configure
     */
    @Override
    public void run(WebApplication app) {
        if (configure) {
            var options = app.options().getGroup(CorsOptions.CORS_GROUP);
            if (options != null) {
                configure(options);
            }
        }
        var config = builder.build();
        var task = new CorsTask(
                config,
                parser == null ? HttpMethod::of : parser,
                allMethods == null ? HttpMethod.all().values() : allMethods
        );
        app.configurer().add(task);
    }
}
