package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;

import java.util.Set;

/**
 *
 * @param <V>
 */
public interface AccessConfigurer<V> extends Resettable {

    /**
     *
     * @return
     */
    Set<V> allowed();

    /**
     *
     * @param values
     * @return
     */
    AccessConfigurer<V> allowed(Set<V> values);

    /**
     *
     * @return
     */
    AccessConfigurer<V> allowAny();

    /**
     *
     * @param value
     * @return
     */
    AccessConfigurer<V> allow(V value);

    /**
     *
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    AccessConfigurer<V> allow(V... values);

    /**
     *
     * @param values
     * @return
     */
    AccessConfigurer<V> allow(Iterable<V> values);

    /**
     *
     * @return
     */
    AccessConfigurer<V> denyAny();

    /**
     *
     * @param value
     * @return
     */
    AccessConfigurer<V> deny(V value);

    /**
     *
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    AccessConfigurer<V> deny(V... values);

    /**
     *
     * @param values
     * @return
     */
    AccessConfigurer<V> deny(Iterable<V> values);
}
