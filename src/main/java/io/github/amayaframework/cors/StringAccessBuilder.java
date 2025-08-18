package io.github.amayaframework.cors;

import java.util.Collections;
import java.util.Set;

final class StringAccessBuilder extends AbstractAccessConfigurer<String, AccessConfigurer<String>> {

    Set<String> build() {
        if (allowed == null) {
            return null;
        }
        var allowed = this.allowed;
        reset();
        return Collections.unmodifiableSet(Util.toLowerCase(allowed));
    }
}
