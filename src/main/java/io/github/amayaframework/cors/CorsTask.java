package io.github.amayaframework.cors;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.context.HttpResponse;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CorsTask implements TaskConsumer<HttpContext> {
    protected final CorsConfig config;
    protected final HttpMethodParser parser;
    protected final String allMethods;
    protected final String methods;
    protected final String headers;
    protected final String exposed;
    protected final String maxAge;

    public CorsTask(CorsConfig config, HttpMethodParser parser, Iterable<HttpMethod> allMethods) {
        this.config = config;
        this.parser = parser;
        this.methods = StringUtil.render(config.allowedMethods);
        this.allMethods = StringUtil.render(allMethods);
        this.headers = StringUtil.render(config.allowedHeaders);
        this.exposed = StringUtil.render(config.exposedHeaders);
        this.maxAge = config.maxAge < 0 ? null : Integer.toString(config.maxAge);
    }

    public CorsTask(CorsConfig config) {
        this(config, HttpMethod::of, HttpMethod.all().values());
    }

    private boolean checkOrigin(String origin) {
        var allowedOrigins = config.allowedOrigins;
        if (allowedOrigins != null && allowedOrigins.contains(origin)) {
            return true;
        }
        var allowedRegexes = config.allowedRegexes;
        if (allowedRegexes != null && !allowedRegexes.isEmpty()) {
            for (var pattern : allowedRegexes) {
                if (pattern.matcher(origin).matches()) {
                    return true;
                }
            }
            return false;
        }
        return allowedOrigins == null;
    }

    private String renderOrigin(String origin) {
        if (config.allowedOrigins == null && config.allowedRegexes == null) {
            return "*";
        }
        return origin;
    }

    private boolean checkHeaders(String headers) {
        if (config.allowedHeaders == null) {
            return true;
        }
        var split = Tokenizers.split(headers, ",");
        var allowed = config.allowedHeaders;
        for (var header : split) {
            if (!allowed.contains(header.strip().toLowerCase(Locale.ENGLISH))) {
                return false;
            }
        }
        return true;
    }

    private void handlePreflight(HttpRequest req, HttpResponse res, String origin, String method) {
        // Pre-set NO_CONTENT
        res.setStatus(HttpCode.NO_CONTENT);
        // Check for origin
        if (!checkOrigin(origin)) {
            return;
        }
        // Check for method
        var allowedMethods = config.allowedMethods;
        var requestedMethod = parser.parse(method);
        if (requestedMethod == null || (allowedMethods != null && !allowedMethods.contains(requestedMethod))) {
            return;
        }
        // Check for allowed headers
        var requestedHeaders = req.getHeader(CorsHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (requestedHeaders != null && !checkHeaders(requestedHeaders)) {
            return;
        }
        // Render max-age
        if (maxAge != null) {
            res.setHeader(CorsHeaders.ACCESS_CONTROL_MAX_AGE, maxAge);
        }
        // Split logic: with and without credentials
        if (config.allowCredentials) {
            // Render credentials
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            // Render origin
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            // Render methods
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, methods == null ? allMethods : methods);
            // Render headers
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headers == null ? requestedHeaders : headers);
            // Set vary for creds
            res.setHeader(ProxyHeaders.VARY, ProxyHeaders.CREDENTIALS_PREFLIGHT_VALUE);
        } else {
            // Render origin
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, renderOrigin(origin));
            // Render methods
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, methods == null ? "*" : methods);
            // Render headers
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headers == null ? "*" : headers);
            // Set basic Vary
            res.setHeader(ProxyHeaders.VARY, ProxyHeaders.ORIGIN_VALUE);
        }
    }

    private void handlePlainRequest(HttpResponse res, String origin) {
        // Set Vary header
        res.setHeader(ProxyHeaders.VARY, ProxyHeaders.ORIGIN_VALUE);
        // Split logic: with and without credentials
        if (config.allowCredentials) {
            // Render credentials
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            // Render origin
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            // Render exposed headers
            if (exposed != null) {
                res.setHeader(CorsHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, exposed);
            }
        } else {
            // Render origin
            res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, renderOrigin(origin));
            // Render exposed headers
            res.setHeader(CorsHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, exposed == null ? "*" : exposed);
        }
    }

    @Override
    public void run(HttpContext context, Task<HttpContext> next) throws Throwable {
        var req = context.request();
        // Try to find Origin
        var origin = req.getHeader(CorsHeaders.ORIGIN);
        if (origin == null) {
            next.run(context);
            return;
        }
        var requestedMethod = req.getHeader(CorsHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        // Handle preflight
        if (req.getMethod().equals(HttpMethod.OPTIONS) && requestedMethod != null) {
            handlePreflight(req, context.response(), origin, requestedMethod);
            return;
        }
        // Handle plain request
        // Skip not-allowed request
        if (!checkOrigin(origin)) {
            return;
        }
        handlePlainRequest(context.response(), origin);
        // Do next
        next.run(context);
    }

    @Override
    public CompletableFuture<Void> runAsync(HttpContext context, Task<HttpContext> next) {
        var req = context.request();
        // Try to find Origin
        var origin = req.getHeader(CorsHeaders.ORIGIN);
        if (origin == null) {
            return next.runAsync(context);
        }
        var requestedMethod = req.getHeader(CorsHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        // Check for preflight
        if (req.getMethod().equals(HttpMethod.OPTIONS) && requestedMethod != null) {
            handlePreflight(req, context.response(), origin, requestedMethod);
            return CompletableFuture.completedFuture(null);
        }
        // Handle plain request
        // Skip not-allowed request
        var allowedOrigins = config.allowedOrigins;
        if (allowedOrigins != null && !allowedOrigins.contains(origin)) {
            return next.runAsync(context);
        }
        handlePlainRequest(context.response(), origin);
        // Do next
        return next.runAsync(context);
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public boolean isUni() {
        return true;
    }
}
