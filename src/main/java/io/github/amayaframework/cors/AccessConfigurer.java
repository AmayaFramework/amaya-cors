package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;

import java.util.Set;

public interface AccessConfigurer<V> extends Resettable {

    Set<V> allowed();

    AccessConfigurer<V> allowed(Set<V> values);

    AccessConfigurer<V> allowAny();

    AccessConfigurer<V> allow(V value);

    @SuppressWarnings("unchecked")
    AccessConfigurer<V> allow(V... values);

    AccessConfigurer<V> allow(Iterable<V> values);

    AccessConfigurer<V> denyAny();

    AccessConfigurer<V> deny(V value);

    @SuppressWarnings("unchecked")
    AccessConfigurer<V> deny(V... values);

    AccessConfigurer<V> deny(Iterable<V> values);
}
