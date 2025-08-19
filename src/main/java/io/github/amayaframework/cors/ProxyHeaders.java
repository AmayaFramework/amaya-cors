package io.github.amayaframework.cors;

import java.util.Locale;

/**
 * Defines headers used for proxying or CORS-specific handling, such as
 * {@code Vary} headers and preflight credentials handling.
 */
public final class ProxyHeaders {
    private ProxyHeaders() {
    }

    public static final String VARY = "Vary";

    /**
     * Value representing the Origin header in lowercase (for Vary).
     */
    public static final String ORIGIN_VALUE = CorsHeaders.ORIGIN.toLowerCase(Locale.ENGLISH);

    /**
     * Vary header value used when credentials are involved in preflight requests.
     */
    public static final String CREDENTIALS_PREFLIGHT_VALUE = StringUtil.render(
            CorsHeaders.ORIGIN,
            CorsHeaders.ACCESS_CONTROL_REQUEST_METHOD,
            CorsHeaders.ACCESS_CONTROL_REQUEST_HEADERS
    );
}
