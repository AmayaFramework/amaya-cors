package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class CorsConfig {
    final Set<String> allowedOrigins;
    final List<Pattern> allowedRegexes;
    final Set<HttpMethod> allowedMethods;
    final Set<String> allowedHeaders;
    final Set<String> exposedHeaders;
    final boolean allowCredentials;
    final int maxAge;

    public CorsConfig(Set<String> allowedOrigins,
                      List<Pattern> allowedRegexes,
                      Set<HttpMethod> allowedMethods,
                      Set<String> allowedHeaders,
                      Set<String> exposedHeaders,
                      boolean allowCredentials,
                      int maxAge) {
        this.allowedOrigins = allowedOrigins;
        this.allowedRegexes = allowedRegexes;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.exposedHeaders = exposedHeaders;
        this.allowCredentials = allowCredentials;
        this.maxAge = maxAge;
    }

    public Set<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public List<Pattern> getAllowedRegexes() {
        return allowedRegexes;
    }

    public Set<HttpMethod> getAllowedMethods() {
        return allowedMethods;
    }

    public Set<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public Set<String> getExposedHeaders() {
        return exposedHeaders;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public int getMaxAge() {
        return maxAge;
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
