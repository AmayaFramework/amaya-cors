package io.github.amayaframework.cors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractAccessConfigurer<V, C extends AccessConfigurer<V>> implements AccessConfigurer<V> {
    protected Set<V> allowed;

    protected boolean ensure() {
        if (allowed == null) {
            allowed = new HashSet<>();
            return false;
        }
        return true;
    }

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
