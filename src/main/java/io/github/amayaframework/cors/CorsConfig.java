package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 */
public final class CorsConfig {
    Set<String> allowedOrigins;
    List<Pattern> allowedRegexes;
    Set<HttpMethod> allowedMethods;
    Set<String> allowedHeaders;
    Set<String> exposedHeaders;
    boolean allowCredentials;
    int maxAge;

    public CorsConfig() {
        this.allowedOrigins = null;
        this.allowedRegexes = null;
        this.allowedMethods = null;
        this.allowedHeaders = null;
        this.allowCredentials = false;
        this.maxAge = -1;
    }

    public Set<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(Set<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<Pattern> getAllowedRegexes() {
        return allowedRegexes;
    }

    public void setAllowedRegexes(List<Pattern> allowedRegexes) {
        this.allowedRegexes = allowedRegexes;
    }

    public Set<HttpMethod> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(Set<HttpMethod> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public Set<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(Set<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public Set<String> getExposedHeaders() {
        return exposedHeaders;
    }

    public void setExposedHeaders(Set<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

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
