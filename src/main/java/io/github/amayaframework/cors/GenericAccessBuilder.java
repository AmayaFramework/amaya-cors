package io.github.amayaframework.cors;

import java.util.Collections;
import java.util.Set;

final class GenericAccessBuilder<V> extends AbstractAccessConfigurer<V, AccessConfigurer<V>> {

    Set<V> build() {
        if (allowed == null) {
            return null;
        }
        var allowed = this.allowed;
        reset();
        return Collections.unmodifiableSet(allowed);
    }
}
