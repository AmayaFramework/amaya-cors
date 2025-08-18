package io.github.amayaframework.cors;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.options.OptionSet;
import io.github.amayaframework.web.WebApplication;

/**
 *
 */
public final class CorsApplicationConfigurer implements Runnable1<WebApplication> {
    private final CorsConfigBuilder builder;
    private final boolean configure;
    private HttpMethodParser parser;
    private Iterable<HttpMethod> allMethods;

    /**
     *
     * @param configure
     */
    public CorsApplicationConfigurer(boolean configure) {
        this.builder = new CorsConfigBuilder();
        this.configure = configure;
    }

    /**
     *
     */
    public CorsApplicationConfigurer() {
        this(false);
    }

    /**
     *
     * @return
     */
    public CorsConfigurer getConfigurer() {
        return builder;
    }

    /**
     *
     * @return
     */
    public HttpMethodParser getParser() {
        return parser;
    }

    /**
     *
     * @param parser
     */
    public void setParser(HttpMethodParser parser) {
        this.parser = parser;
    }

    /**
     *
     * @return
     */
    public Iterable<HttpMethod> getAllMethods() {
        return allMethods;
    }

    /**
     *
     * @param allMethods
     */
    public void setAllMethods(Iterable<HttpMethod> allMethods) {
        this.allMethods = allMethods;
    }

    private void configure(OptionSet options) {
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
            var allowedOrigins =  builder.allowedOrigins();
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
     *
     * @param app
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
