package io.github.amayaframework.cors;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class CorsConfigBuilder extends AbstractCorsConfigurer<CorsConfigBuilder> {

    private static Set<String> toLowerCase(Set<String> set) {
        if (set == null) {
            return null;
        }
        return set.stream().map(h -> h.toLowerCase(Locale.ENGLISH)).collect(Collectors.toSet());
    }

    private List<Pattern> compileRegexes() {
        if (allowedRegexes == null) {
            return null;
        }
        var ret = new ArrayList<Pattern>(allowedRegexes.size());
        for (var raw : allowedRegexes) {
            ret.add(Pattern.compile(raw));
        }
        return ret;
    }

    public CorsConfig build() {
        try {
            return new CorsConfig(
                    allowedOrigins,
                    compileRegexes(),
                    allowedMethods,
                    toLowerCase(allowedHeaders),
                    toLowerCase(exposedHeaders),
                    allowCredentials,
                    maxAge
            );
        } finally {
            reset();
        }
    }
}
