package io.github.amayaframework.cors;

import java.util.Locale;

/**
 *
 */
public final class ProxyHeaders {
    private ProxyHeaders() {
    }

    /**
     *
     */
    public static final String VARY = "Vary";

    /**
     *
     */
    public static final String ORIGIN_VALUE = CorsHeaders.ORIGIN.toLowerCase(Locale.ENGLISH);

    /**
     *
     */
    public static final String CREDENTIALS_PREFLIGHT_VALUE = StringUtil.render(
            CorsHeaders.ORIGIN,
            CorsHeaders.ACCESS_CONTROL_REQUEST_METHOD,
            CorsHeaders.ACCESS_CONTROL_REQUEST_HEADERS
    );
}
