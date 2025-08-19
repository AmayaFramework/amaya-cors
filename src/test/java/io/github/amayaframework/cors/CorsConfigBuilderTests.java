package io.github.amayaframework.cors;

import io.github.amayaframework.http.HttpMethod;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public final class CorsConfigBuilderTests {

    @Test
    public void corsConfigBuilderAllowedOriginsWorks() {
        var builder = new CorsConfigBuilder();
        builder.allowedOrigins().allow("http://a.com");
        var origins = builder.build().getAllowedOrigins();
        assertEquals(Set.of("http://a.com"), origins);
    }

    @Test
    public void corsConfigBuilderAllowedMethodsWorks() {
        var builder = new CorsConfigBuilder();
        builder.allowedMethods().allow(HttpMethod.GET, HttpMethod.POST);
        var methods = builder.build().getAllowedMethods();
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.POST), methods);
    }

    @Test
    public void corsConfigBuilderAllowedHeadersWorks() {
        var builder = new CorsConfigBuilder();
        builder.allowedHeaders().allow("X-Test", "X-Another");
        var headers = builder.build().getAllowedHeaders();
        assertEquals(Set.of("x-test", "x-another"), headers);
    }

    @Test
    public void corsConfigBuilderExposedHeadersWorks() {
        var builder = new CorsConfigBuilder();
        builder.exposedHeaders().allow("X-Expose");
        var exposed = builder.build().getExposedHeaders();
        assertEquals(Set.of("x-expose"), exposed);
    }

    @Test
    public void corsConfigBuilderAllowAnyOverridesSpecific() {
        var builder = new CorsConfigBuilder();
        builder.allowedOrigins().allow("http://a.com");
        builder.allowedMethods().allow(HttpMethod.GET);
        builder.allowedHeaders().allow("X-Test");
        builder.exposedHeaders().allow("X-Expose");

        builder.allowAny(); // должен сбросить все ограничения

        var config = builder.build();
        assertNull(config.getAllowedOrigins());
        assertNull(config.getAllowedMethods());
        assertNull(config.getAllowedHeaders());
        assertNull(config.getExposedHeaders());
    }

    @Test
    public void corsConfigBuilderAllowCredentialsWorks() {
        var builder = new CorsConfigBuilder();
        builder.allowCredentials(true);
        assertTrue(builder.build().isAllowCredentials());

        builder.allowCredentials(false);
        assertFalse(builder.build().isAllowCredentials());
    }

    @Test
    public void corsConfigBuilderMaxAgeWorks() {
        var builder = new CorsConfigBuilder();
        builder.maxAge(500);
        assertEquals(500, builder.build().getMaxAge());
    }

    @Test
    public void corsConfigBuilderResetClearsEverything() {
        var builder = new CorsConfigBuilder();
        builder.allowedOrigins().allow("http://a.com");
        builder.allowedMethods().allow(HttpMethod.GET);
        builder.allowedHeaders().allow("X-Test");
        builder.exposedHeaders().allow("X-Expose");
        builder.allowCredentials(true);
        builder.maxAge(123);

        builder.reset(); // должен вернуть к дефолту

        var config = builder.build();
        assertNull(config.getAllowedOrigins());
        assertNull(config.getAllowedMethods());
        assertNull(config.getAllowedHeaders());
        assertNull(config.getExposedHeaders());
        assertFalse(config.isAllowCredentials());
        assertEquals(CorsDefaults.MAX_AGE, config.getMaxAge());
    }

    @Test
    public void corsConfigBuilderFluentApiChaining() {
        var builder = new CorsConfigBuilder();
        builder.allowedOrigins().allow("http://a.com")
               .allow("http://b.com");
        builder.allowedMethods().allow(HttpMethod.GET)
               .allow(HttpMethod.POST);
        builder.allowedHeaders().allow("X-Test")
               .allow("X-Another");
        builder.exposedHeaders().allow("X-Expose1")
               .allow("X-Expose2");
        builder.allowCredentials(true);
        builder.maxAge(200);

        var config = builder.build();
        assertEquals(Set.of("http://a.com", "http://b.com"), config.getAllowedOrigins());
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.POST), config.getAllowedMethods());
        assertEquals(Set.of("x-test", "x-another"), config.getAllowedHeaders());
        assertEquals(Set.of("x-expose1", "x-expose2"), config.getExposedHeaders());
        assertTrue(config.isAllowCredentials());
        assertEquals(200, config.getMaxAge());
    }
}
