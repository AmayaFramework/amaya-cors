package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

/**
 * Strategy interface for parsing HTTP method names into {@link HttpMethod} values.
 * <p>
 * Implementations may provide custom resolution logic.
 */
@FunctionalInterface
public interface HttpMethodParser {

    /**
     * Parses a string representation of an HTTP method into a {@link HttpMethod}.
     *
     * @param method the HTTP method name (e.g. {@code "GET"}, {@code "POST"})
     * @return the corresponding {@link HttpMethod} instance or null, if the method name is not recognized
     */
    HttpMethod parse(String method);
}
