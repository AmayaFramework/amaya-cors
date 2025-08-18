package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;

public interface HttpMethodParser {

    HttpMethod parse(String method);
}
