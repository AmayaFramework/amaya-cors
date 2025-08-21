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

/**
 * A {@link TaskConsumer} implementation providing
 * Cross-Origin Resource Sharing (CORS) handling for HTTP requests.
 * <p>
 * This task intercepts incoming requests, checks the {@code Origin} header,
 * and applies the configured CORS policy. It supports both simple
 * requests and preflight (OPTIONS) requests.
 */
public class CorsTask implements TaskConsumer<HttpContext> {

    /**
     * CORS configuration defining allowed origins, methods, headers, and other options.
     */
    protected final CorsConfig config;

    /**
     * Parser used to resolve {@link HttpMethod} from a string.
     * <p>
     * By default, uses {@link HttpMethod#of(String)} to parse HTTP methods.
     */
    protected final HttpMethodParser parser;

    /**
     * String representation of all supported HTTP methods.
     */
    protected final String allMethods;

    /**
     * String representation of explicitly allowed HTTP methods from {@link CorsConfig}.
     */
    protected final String methods;

    /**
     * String representation of explicitly allowed request headers from {@link CorsConfig}.
     */
    protected final String headers;

    /**
     * String representation of exposed response headers from {@link CorsConfig}.
     */
    protected final String exposed;

    /**
     * String representation of {@code Access-Control-Max-Age} header value,
     * or {@code null} if not configured.
     */
    protected final String maxAge;

    /**
     * Creates a new {@code CorsTask} with the given configuration.
     *
     * @param config     the CORS configuration
     * @param parser     the HTTP method parser
     * @param allMethods iterable of all available HTTP methods
     */
    public CorsTask(CorsConfig config, HttpMethodParser parser, Iterable<HttpMethod> allMethods) {
        this.config = config;
        this.parser = parser;
        this.methods = StringUtil.render(config.allowedMethods);
        this.allMethods = StringUtil.render(allMethods);
        this.headers = StringUtil.render(config.allowedHeaders);
        this.exposed = StringUtil.render(config.exposedHeaders);
        this.maxAge = config.maxAge < 0 ? null : Integer.toString(config.maxAge);
    }

    /**
     * Creates a new {@code CorsTask} with the given configuration,
     * using default {@link HttpMethod} parser and all methods.
     *
     * @param config the CORS configuration
     */
    public CorsTask(CorsConfig config) {
        this(config, HttpMethod::of, HttpMethod.all().values());
    }

    /**
     * Checks whether the given origin is allowed by this configuration.
     *
     * @param origin request origin
     * @return {@code true} if allowed, {@code false} otherwise
     */
    protected boolean checkOrigin(String origin) {
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

    /**
     * Renders the origin value to be sent in the response.
     * <p>
     * Returns {@code *} if no restrictions are set (both allowed origins and regex rules are {@code null}).
     *
     * @param origin request origin
     * @return rendered origin string
     */
    protected String renderOrigin(String origin) {
        if (config.allowedOrigins == null && config.allowedRegexes == null) {
            return "*";
        }
        return origin;
    }


    /**
     * Checks whether this configuration allows the requested headers.
     *
     * @param headers comma-separated list of request headers
     * @return {@code true} if all headers are allowed, {@code false} otherwise
     */
    protected boolean checkHeaders(String headers) {
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

    /**
     * Handles a CORS preflight (OPTIONS) request.
     * <p>
     * Validates origin, method, and headers against {@link CorsConfig},
     * then writes appropriate CORS response headers.
     *
     * @param req    the HTTP request
     * @param res    the HTTP response
     * @param origin request origin
     * @param method requested method
     */
    protected void handlePreflight(HttpRequest req, HttpResponse res, String origin, String method) {
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
            if (headers == null) {
                if (requestedHeaders != null) {
                    res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestedHeaders);
                }
            } else {
                res.setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headers);
            }
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

    /**
     * Handles a simple (non-preflight) CORS request.
     * <p>
     * Writes {@code Access-Control-Allow-Origin}, {@code Access-Control-Allow-Credentials},
     * and {@code Access-Control-Expose-Headers} as configured.
     *
     * @param res    the HTTP response
     * @param origin request origin
     */
    protected void handlePlainRequest(HttpResponse res, String origin) {
        var vary = res.getHeader(ProxyHeaders.VARY);
        if (vary == null) {
            res.setHeader(ProxyHeaders.VARY, ProxyHeaders.ORIGIN_VALUE);
        } else {
            res.setHeader(ProxyHeaders.VARY, vary + "," + ProxyHeaders.ORIGIN_VALUE);
        }
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

    /**
     * Executes this CORS task synchronously.
     * Intercepts requests, checks for preflight, and applies CORS headers.
     *
     * @param context the HTTP context
     * @param next    the next task in the chain
     * @throws Throwable if the next task fails
     */
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
        // Handle plain request if origin allowed
        if (checkOrigin(origin)) {
            handlePlainRequest(context.response(), origin);
        }
        // Do next
        next.run(context);
    }

    /**
     * Executes this CORS task asynchronously.
     * Intercepts requests, checks for preflight, and applies CORS headers.
     *
     * @param context the HTTP context
     * @param next    the next task in the chain
     * @return a {@link CompletableFuture} representing completion
     */
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
        // Handle plain request if origin allowed
        if (checkOrigin(origin)) {
            handlePlainRequest(context.response(), origin);
        }
        // Do next
        return next.runAsync(context);
    }

    /**
     * Returns {@code true} because this task supports synchronous execution.
     *
     * @return {@code true}
     */
    @Override
    public boolean isSync() {
        return true;
    }

    /**
     * Returns {@code true} because this task supports asynchronous execution.
     *
     * @return {@code true}
     */
    @Override
    public boolean isAsync() {
        return true;
    }

    /**
     * Returns {@code true} because this task supports both synchronous and asynchronous execution.
     *
     * @return {@code true}
     */
    @Override
    public boolean isUni() {
        return true;
    }
}
