package io.github.amayaframework.cors;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.options.OptionSet;
import io.github.amayaframework.web.WebApplication;

public final class CorsApplicationConfigurer implements Runnable1<WebApplication> {
    private final CorsConfigBuilder builder;
    private final boolean parseOptions;
    private HttpMethodParser parser;
    private Iterable<HttpMethod> allMethods;

    public CorsApplicationConfigurer(boolean parseOptions) {
        this.builder = new CorsConfigBuilder();
        this.parseOptions = parseOptions;
    }

    public CorsApplicationConfigurer() {
        this(false);
    }

    public CorsConfigurer getConfigurer() {
        return builder;
    }

    public HttpMethodParser getParser() {
        return parser;
    }

    public void setParser(HttpMethodParser parser) {
        this.parser = parser;
    }

    public Iterable<HttpMethod> getAllMethods() {
        return allMethods;
    }

    public void setAllMethods(Iterable<HttpMethod> allMethods) {
        this.allMethods = allMethods;
    }

    private void configureCors(OptionSet options) {
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
            builder.allowAnyOrigin();
        } else {
            builder.allowOrigins(options.get(CorsOptions.ALLOWED_ORIGINS));
            var regexes = options.get(CorsOptions.ORIGIN_REGEXES);
            if (regexes != null) {
                for (var regex : regexes) {
                    if (regex == null) {
                        continue;
                    }
                    builder.addOriginRegex(regex);
                }
            }
        }
        if (options.asKey(CorsOptions.ALLOW_ANY_METHOD)) {
            builder.allowAnyMethod();
        } else {
            builder.allowMethods(options.get(CorsOptions.ALLOWED_METHODS));
        }
        if (options.asKey(CorsOptions.ALLOW_ANY_HEADER)) {
            builder.allowAnyHeader();
        } else {
            builder.allowHeaders(options.get(CorsOptions.ALLOWED_HEADERS));
        }
        if (options.asKey(CorsOptions.EXPOSE_ANY_HEADER)) {
            builder.exposeAnyHeader();
        } else {
            builder.exposeHeaders(options.get(CorsOptions.EXPOSED_HEADERS));
        }
    }

    @Override
    public void run(WebApplication app) {
        var options = app.options().getGroup(CorsOptions.CORS_GROUP);
        if (parseOptions) {
            configureCors(options);
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
