package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

/**
 *
 */
@FunctionalInterface
public interface HttpMethodParser {

    /**
     *
     * @param method
     * @return
     */
    HttpMethod parse(String method);
}
