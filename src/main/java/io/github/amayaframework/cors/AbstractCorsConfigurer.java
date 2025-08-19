package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.http.HttpMethod;

abstract class AbstractCorsConfigurer<C extends CorsConfigurer> implements CorsConfigurer {
    protected CompileOriginBuilder originsBuilder;
    protected GenericAccessBuilder<HttpMethod> methodsBuilder;
    protected StringAccessBuilder headersBuilder;
    protected StringAccessBuilder exposedBuilder;
    protected boolean allowCredentials;
    protected int maxAge;

    protected AbstractCorsConfigurer() {
        allowCredentials = CorsDefaults.ALLOW_CREDENTIALS;
        maxAge = CorsDefaults.MAX_AGE;
    }

    private static void reset(Resettable resettable) {
        if (resettable != null) {
            resettable.reset();
        }
    }

    @Override
    public void reset() {
        reset(originsBuilder);
        reset(methodsBuilder);
        reset(headersBuilder);
        reset(exposedBuilder);
        allowCredentials = CorsDefaults.ALLOW_CREDENTIALS;
        maxAge = CorsDefaults.MAX_AGE;
    }

    @Override
    public OriginConfigurer allowedOrigins() {
        if (originsBuilder == null) {
            originsBuilder = new CompileOriginBuilder();
        }
        return originsBuilder;
    }

    @Override
    public AccessConfigurer<HttpMethod> allowedMethods() {
        if (methodsBuilder == null) {
            methodsBuilder = new GenericAccessBuilder<>();
        }
        return methodsBuilder;
    }

    @Override
    public AccessConfigurer<String> allowedHeaders() {
        if (headersBuilder == null) {
            headersBuilder = new StringAccessBuilder();
        }
        return headersBuilder;
    }

    @Override
    public AccessConfigurer<String> exposedHeaders() {
        if (exposedBuilder == null) {
            exposedBuilder = new StringAccessBuilder();
        }
        return exposedBuilder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowAny() {
        originsBuilder.allowAny();
        methodsBuilder.allowAny();
        headersBuilder.allowAny();
        exposedBuilder.allowAny();
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
