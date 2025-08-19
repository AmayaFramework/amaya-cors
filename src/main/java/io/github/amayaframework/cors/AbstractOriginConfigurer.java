package io.github.amayaframework.cors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Base implementation for {@link OriginConfigurer} providing common functionality
 * for managing allowed origins and origin regex patterns.
 *
 * @param <O> the concrete type of the origin configurer
 */
public abstract class AbstractOriginConfigurer<O extends OriginConfigurer>
        extends AbstractAccessConfigurer<String, O> implements OriginConfigurer {

    /**
     * Set of regex patterns used to match allowed origins.
     */
    protected Set<String> regexes;

    @Override
    public void reset() {
        allowed = null;
        regexes = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public O allowAny() {
        allowed = null;
        regexes = null;
        return (O) this;
    }

    @Override
    public Set<String> allowedRegexes() {
        return regexes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public O allowedRegexes(Set<String> regexes) {
        this.regexes = regexes;
        return (O) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public O addRegex(String regex) {
        Objects.requireNonNull(regex);
        if (regexes == null) {
            regexes = new HashSet<>();
        }
        regexes.add(regex);
        return (O) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public O removeRegex(String regex) {
        if (regex != null && regexes != null) {
            regexes.remove(regex);
        }
        return (O) this;
    }
}
