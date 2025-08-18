package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractCorsConfigurer<C extends CorsConfigurer> implements CorsConfigurer {
    protected Set<String> allowedOrigins;
    protected Set<String> allowedRegexes;
    protected Set<HttpMethod> allowedMethods;
    protected Set<String> allowedHeaders;
    protected Set<String> exposedHeaders;
    protected boolean allowCredentials;
    protected int maxAge;

    protected AbstractCorsConfigurer() {
        reset();
    }

    @Override
    public void reset() {
        allowedOrigins = CorsDefaults.ALLOWED_ORIGINS;
        allowedRegexes = null;
        allowedMethods = new HashSet<>(CorsDefaults.ALLOWED_METHODS);
        allowedHeaders = new HashSet<>(CorsDefaults.ALLOWED_HEADERS);
        exposedHeaders = new HashSet<>();
        allowCredentials = CorsDefaults.ALLOW_CREDENTIALS;
        maxAge = CorsDefaults.MAX_AGE;
    }

    private void ensureOrigins() {
        if (allowedOrigins == null) {
            allowedOrigins = new HashSet<>();
        }
    }

    private void ensureAllowedRegexes() {
        if (allowedRegexes == null) {
            allowedRegexes = new HashSet<>();
        }
    }

    private void ensureMethods() {
        if (allowedMethods == null) {
            allowedMethods = new HashSet<>();
        }
    }

    private void ensureAllowedHeaders() {
        if (allowedHeaders == null) {
            allowedHeaders = new HashSet<>();
        }
    }

    private void ensureExposedHeaders() {
        if (exposedHeaders == null) {
            exposedHeaders = new HashSet<>();
        }
    }

    private static <V> void allow(Set<V> set, V[] values) {
        if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.add(value);
            }
        }
    }

    private static <V> void allow(Set<V> set, Iterable<V> values) {
        if (values instanceof Collection) {
            set.addAll((Collection<V>) values);
            set.removeIf(Objects::isNull);
        } else if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.add(value);
            }
        }
    }

    private static <V> void deny(Set<V> set, V[] values) {
        if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.remove(value);
            }
        }
    }

    private static <V> void deny(Set<V> set, Iterable<V> values) {
        if (values instanceof Collection) {
            set.removeAll((Collection<V>) values);
        } else if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.remove(value);
            }
        }
    }

    // Origins

    @Override
    public Set<String> allowedOrigins() {
        return allowedOrigins;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowedOrigins(Set<String> origins) {
        allowedOrigins = origins;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowAnyOrigin() {
        allowedOrigins = null;
        allowedRegexes = null;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowOrigin(String origin) {
        Objects.requireNonNull(origin);
        ensureOrigins();
        allowedOrigins.add(origin);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowOrigins(String... origins) {
        ensureOrigins();
        allow(allowedOrigins, origins);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowOrigins(Iterable<String> origins) {
        ensureOrigins();
        allow(allowedOrigins, origins);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyAnyOrigin() {
        if (allowedOrigins == null || !allowedOrigins.isEmpty()) {
            allowedOrigins = new HashSet<>();
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyOrigin(String origin) {
        if (allowedOrigins == null) {
            allowedOrigins = new HashSet<>();
        } else if (origin != null) {
            allowedOrigins.remove(origin);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyOrigins(String... origins) {
        if (allowedOrigins == null) {
            allowedOrigins = new HashSet<>();
        } else {
            deny(allowedOrigins, origins);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyOrigins(Iterable<String> origins) {
        if (allowedOrigins == null) {
            allowedOrigins = new HashSet<>();
        } else {
            deny(allowedOrigins, origins);
        }
        return (C) this;
    }

    // Regex origins

    @Override
    public Set<String> allowedOriginRegexes() {
        return allowedRegexes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowedOriginRegexes(Set<String> origins) {
        allowedRegexes = origins;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C addOriginRegex(String regex) {
        Objects.requireNonNull(regex);
        ensureAllowedRegexes();
        allowedRegexes.add(regex);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C removeOriginRegex(String regex) {
        if (allowedRegexes != null && regex != null) {
            allowedRegexes.remove(regex);
        }
        return (C) this;
    }

    // Methods

    @Override
    public Set<HttpMethod> allowedMethods() {
        return allowedMethods;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowedMethods(Set<HttpMethod> methods) {
        allowedMethods = methods;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowAnyMethod() {
        allowedMethods = null;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowMethod(HttpMethod method) {
        Objects.requireNonNull(method);
        ensureMethods();
        allowedMethods.add(method);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowMethods(HttpMethod... methods) {
        ensureMethods();
        allow(allowedMethods, methods);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowMethods(Iterable<HttpMethod> methods) {
        ensureMethods();
        allow(allowedMethods, methods);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyAnyMethod() {
        if (allowedMethods == null || !allowedMethods.isEmpty()) {
            allowedMethods = new HashSet<>();
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyMethod(HttpMethod method) {
        if (allowedMethods == null) {
            allowedMethods = new HashSet<>();
        } else if (method != null) {
            allowedMethods.remove(method);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyMethods(HttpMethod... methods) {
        if (allowedMethods == null) {
            allowedMethods = new HashSet<>();
        } else {
            deny(allowedMethods, methods);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyMethods(Iterable<HttpMethod> methods) {
        if (allowedMethods == null) {
            allowedMethods = new HashSet<>();
        } else {
            deny(allowedMethods, methods);
        }
        return (C) this;
    }

    // Headers (allowed)

    @Override
    public Set<String> allowedHeaders() {
        return allowedHeaders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowedHeaders(Set<String> headers) {
        allowedHeaders = headers;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowAnyHeader() {
        allowedHeaders = null;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowHeader(String header) {
        Objects.requireNonNull(header);
        ensureAllowedHeaders();
        allowedHeaders.add(header);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowHeaders(String... headers) {
        ensureAllowedHeaders();
        allow(allowedHeaders, headers);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowHeaders(Iterable<String> headers) {
        ensureAllowedHeaders();
        allow(allowedHeaders, headers);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyAnyHeader() {
        if (allowedHeaders == null || !allowedHeaders.isEmpty()) {
            allowedHeaders = new HashSet<>();
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyHeader(String header) {
        if (allowedHeaders == null) {
            allowedHeaders = new HashSet<>();
        } else if (header != null) {
            allowedHeaders.remove(header);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyHeaders(String... headers) {
        if (allowedHeaders == null) {
            allowedHeaders = new HashSet<>();
        } else {
            deny(allowedHeaders, headers);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyHeaders(Iterable<String> headers) {
        if (allowedHeaders == null) {
            allowedHeaders = new HashSet<>();
        } else {
            deny(allowedHeaders, headers);
        }
        return (C) this;
    }

    @Override
    public Set<String> exposedHeaders() {
        return exposedHeaders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C exposedHeaders(Set<String> headers) {
        exposedHeaders = headers;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C exposeAnyHeader() {
        exposedHeaders = null;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C exposeHeader(String header) {
        Objects.requireNonNull(header);
        ensureExposedHeaders();
        exposedHeaders.add(header);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C exposeHeaders(String... headers) {
        ensureExposedHeaders();
        allow(exposedHeaders, headers);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C exposeHeaders(Iterable<String> headers) {
        ensureExposedHeaders();
        allow(exposedHeaders, headers);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C hideAnyHeader() {
        if (exposedHeaders == null || !exposedHeaders.isEmpty()) {
            exposedHeaders = new HashSet<>();
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C hideHeader(String header) {
        if (exposedHeaders == null) {
            exposedHeaders = new HashSet<>();
        } else if (header != null) {
            exposedHeaders.remove(header);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C hideHeaders(String... headers) {
        if (exposedHeaders == null) {
            exposedHeaders = new HashSet<>();
        } else {
            deny(exposedHeaders, headers);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C hideHeaders(Iterable<String> headers) {
        if (exposedHeaders == null) {
            exposedHeaders = new HashSet<>();
        } else {
            deny(exposedHeaders, headers);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowAny() {
        allowedOrigins = null;
        allowedRegexes = null;
        allowedMethods = null;
        allowedHeaders = null;
        exposedHeaders = null;
        return (C) this;
    }

    @Override
    public boolean allowCredentials() {
        return allowCredentials;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowCredentials(boolean allow) {
        allowCredentials = allow;
        return (C) this;
    }

    @Override
    public int maxAge() {
        return maxAge;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C maxAge(int seconds) {
        maxAge = seconds;
        return (C) this;
    }
}
