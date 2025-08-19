package io.github.amayaframework.cors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract base class providing common logic for {@link AccessConfigurer} implementations.
 * <p>
 * Handles the internal storage of allowed values and provides default implementations
 * for adding, removing, and resetting access rules.
 *
 * @param <V> the type of value to configure (e.g., {@link String} or {@link io.github.amayaframework.http.HttpMethod})
 * @param <C> the concrete subclass type, used for fluent API chaining
 */
public abstract class AbstractAccessConfigurer<V, C extends AccessConfigurer<V>> implements AccessConfigurer<V> {

    /**
     * Internal storage for currently allowed values.
     * <p>
     * A {@code null} value indicates that all values are allowed.
     */
    protected Set<V> allowed;

    /**
     * Ensures that the internal {@link #allowed} set is initialized.
     *
     * @return {@code true} if the set was already initialized, {@code false} if it was created
     */
    protected boolean ensure() {
        if (allowed == null) {
            allowed = new HashSet<>();
            return false;
        }
        return true;
    }

    /**
     * Ensures that the internal {@link #allowed} set is empty.
     * <p>
     * Resets the set if it was {@code null} or contained elements.
     */
    protected void ensureEmpty() {
        if (allowed == null || !allowed.isEmpty()) {
            allowed = new HashSet<>();
        }
    }

    @Override
    public void reset() {
        allowed = null;
    }

    @Override
    public Set<V> allowed() {
        return allowed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowed(Set<V> values) {
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allowAny() {
        allowed = null;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allow(V value) {
        Objects.requireNonNull(value);
        ensure();
        allowed.add(value);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allow(V... values) {
        ensure();
        Util.allow(allowed, values);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C allow(Iterable<V> values) {
        ensure();
        Util.allow(allowed, values);
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C denyAny() {
        ensureEmpty();
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C deny(V value) {
        if (ensure() && value != null) {
            allowed.remove(value);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C deny(V... values) {
        if (ensure()) {
            Util.deny(allowed, values);
        }
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C deny(Iterable<V> values) {
        if (ensure()) {
            Util.deny(allowed, values);
        }
        return (C) this;
    }
}
