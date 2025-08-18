package io.github.amayaframework.cors;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class CorsConfigBuilder extends AbstractCorsConfigurer<CorsConfigBuilder> {

    private static Set<String> toLowerCase(Set<String> set) {
        if (set == null) {
            return null;
        }
        return set.stream().map(h -> h.toLowerCase(Locale.US)).collect(Collectors.toSet());
    }

    public CorsConfig build() {
        var ret = new CorsConfig(
                allowedOrigins,
                allowedMethods,
                toLowerCase(allowedHeaders),
                toLowerCase(exposedHeaders),
                allowCredentials,
                maxAge
        );
        reset();
        return ret;
    }
}
