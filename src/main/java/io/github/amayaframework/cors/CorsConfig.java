package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Represents a configuration for handling CORS (Cross-Origin Resource Sharing).
 * <p>
 * This class defines rules for allowed origins, methods, headers, and other
 * CORS-related settings. It can be used by {@link CorsTask} or other
 * components to evaluate and enforce cross-origin requests.
 */
public final class CorsConfig {
    Set<String> allowedOrigins;
    List<Pattern> allowedRegexes;
    Set<HttpMethod> allowedMethods;
    Set<String> allowedHeaders;
    Set<String> exposedHeaders;
    boolean allowCredentials;
    int maxAge;

    /**
     * Creates a new {@code CorsConfig} with default values.
     * <p>
     * By default, no origins, methods, or headers are allowed, credentials
     * are disabled, and {@code maxAge} is set to {@code -1}.
     */
    public CorsConfig() {
        this.allowedOrigins = null;
        this.allowedRegexes = null;
        this.allowedMethods = null;
        this.allowedHeaders = null;
        this.allowCredentials = false;
        this.maxAge = -1;
    }

    /**
     * Returns the set of explicitly allowed origins.
     *
     * @return the allowed origins, or {@code null} if not configured
     */
    public Set<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    /**
     * Sets the allowed origins.
     *
     * @param allowedOrigins a set of origins, or {@code null} to disable restriction
     */
    public void setAllowedOrigins(Set<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * Returns the list of regex patterns used to match allowed origins.
     *
     * @return a list of origin patterns, or {@code null} if not configured
     */
    public List<Pattern> getAllowedRegexes() {
        return allowedRegexes;
    }

    /**
     * Sets the list of regex patterns for allowed origins.
     *
     * @param allowedRegexes regex patterns to match origins
     */
    public void setAllowedRegexes(List<Pattern> allowedRegexes) {
        this.allowedRegexes = allowedRegexes;
    }

    /**
     * Returns the allowed HTTP methods.
     *
     * @return the allowed methods, or {@code null} if not configured
     */
    public Set<HttpMethod> getAllowedMethods() {
        return allowedMethods;
    }

    /**
     * Sets the allowed HTTP methods.
     *
     * @param allowedMethods a set of {@link HttpMethod} values
     */
    public void setAllowedMethods(Set<HttpMethod> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    /**
     * Returns the allowed request headers.
     *
     * @return the allowed headers, or {@code null} if not configured
     */
    public Set<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    /**
     * Sets the allowed request headers.
     *
     * @param allowedHeaders a set of header names
     */
    public void setAllowedHeaders(Set<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    /**
     * Returns the headers that should be exposed to clients.
     *
     * @return the exposed headers, or {@code null} if not configured
     */
    public Set<String> getExposedHeaders() {
        return exposedHeaders;
    }

    /**
     * Sets the headers to expose in responses.
     *
     * @param exposedHeaders a set of header names
     */
    public void setExposedHeaders(Set<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    /**
     * Returns the maximum age (in seconds) for caching preflight responses.
     *
     * @return the max age, or {@code -1} if not configured
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the maximum age (in seconds) for caching preflight responses.
     *
     * @param maxAge the number of seconds, or {@code -1} to disable caching
     */
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * Returns whether credentials are allowed in cross-origin requests.
     *
     * @return {@code true} if credentials are allowed, otherwise {@code false}
     */
    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    /**
     * Sets whether credentials should be allowed in cross-origin requests.
     *
     * @param allowCredentials {@code true} to allow, {@code false} to disallow
     */
    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    @Override
    public String toString() {
        return "CorsConfig{" +
                "allowedOrigins=" + allowedOrigins +
                ", allowedMethods=" + allowedMethods +
                ", allowedHeaders=" + allowedHeaders +
                ", exposedHeaders=" + exposedHeaders +
                ", allowCredentials=" + allowCredentials +
                ", maxAge=" + maxAge +
                '}';
    }
}
