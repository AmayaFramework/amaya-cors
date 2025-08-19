package io.github.amayaframework.cors;

import io.github.amayaframework.application.Resettable;

import java.util.Set;

/**
 * Generic contract for configuring access rules in CORS.
 * <p>
 * Provides methods for defining allowed and denied values of type {@code V},
 * supporting fine-grained inclusion/exclusion semantics. Implementations can
 * represent origins, methods, headers, or other CORS-related entities.
 *
 * @param <V> the type of element to configure
 */
public interface AccessConfigurer<V> extends Resettable {

    /**
     * Returns the set of currently allowed values.
     *
     * @return a set of allowed values
     */
    Set<V> allowed();

    /**
     * Replaces the allowed set with the given values.
     *
     * @param values values to allow
     * @return this configurer for chaining
     */
    AccessConfigurer<V> allowed(Set<V> values);

    /**
     * Allows all values without restriction.
     *
     * @return this configurer for chaining
     */
    AccessConfigurer<V> allowAny();

    /**
     * Adds a single allowed value.
     *
     * @param value the value to allow
     * @return this configurer for chaining
     */
    AccessConfigurer<V> allow(V value);

    /**
     * Adds multiple allowed values.
     *
     * @param values the values to allow
     * @return this configurer for chaining
     */
    @SuppressWarnings("unchecked")
    AccessConfigurer<V> allow(V... values);

    /**
     * Adds allowed values from an iterable.
     *
     * @param values iterable of values to allow
     * @return this configurer for chaining
     */
    AccessConfigurer<V> allow(Iterable<V> values);

    /**
     * Denies all values (resets allowed set).
     *
     * @return this configurer for chaining
     */
    AccessConfigurer<V> denyAny();

    /**
     * Removes a single value from the allowed set.
     *
     * @param value the value to deny
     * @return this configurer for chaining
     */
    AccessConfigurer<V> deny(V value);

    /**
     * Removes multiple values from the allowed set.
     *
     * @param values values to deny
     * @return this configurer for chaining
     */
    @SuppressWarnings("unchecked")
    AccessConfigurer<V> deny(V... values);

    /**
     * Removes values provided by an iterable.
     *
     * @param values iterable of values to deny
     * @return this configurer for chaining
     */
    AccessConfigurer<V> deny(Iterable<V> values);
}
