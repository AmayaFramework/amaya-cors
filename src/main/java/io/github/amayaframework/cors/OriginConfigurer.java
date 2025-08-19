package io.github.amayaframework.cors;

import java.util.Set;

/**
 * Specialization of {@link AccessConfigurer} for configuring CORS origins.
 * <p>
 * In addition to exact string matching, supports regular expressions for
 * flexible origin validation.
 */
public interface OriginConfigurer extends AccessConfigurer<String> {

    /**
     * Sets allowed origins.
     *
     * @param values set of origins
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer allowed(Set<String> values);

    /**
     * Allows any origin.
     *
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer allowAny();

    /**
     * Adds a single allowed origin.
     *
     * @param value the origin to allow
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer allow(String value);

    /**
     * Adds multiple allowed origins.
     *
     * @param values origins to allow
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer allow(String... values);

    /**
     * Adds origins from an iterable.
     *
     * @param values iterable of origins
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer allow(Iterable<String> values);

    /**
     * Denies all origins.
     *
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer denyAny();

    /**
     * Denies a specific origin.
     *
     * @param value the origin to deny
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer deny(String value);

    /**
     * Denies multiple origins.
     *
     * @param values origins to deny
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer deny(String... values);

    /**
     * Denies origins from an iterable.
     *
     * @param values iterable of origins
     * @return this configurer for chaining
     */
    @Override
    OriginConfigurer deny(Iterable<String> values);

    /**
     * Returns the set of allowed origin regex patterns.
     *
     * @return a set of regex strings
     */
    Set<String> allowedRegexes();

    /**
     * Sets allowed origin regex patterns.
     *
     * @param regexes regex strings
     * @return this configurer for chaining
     */
    OriginConfigurer allowedRegexes(Set<String> regexes);

    /**
     * Adds an allowed origin regex pattern.
     *
     * @param regex regex to allow
     * @return this configurer for chaining
     */
    OriginConfigurer addRegex(String regex);

    /**
     * Removes an allowed origin regex pattern.
     *
     * @param regex regex to remove
     * @return this configurer for chaining
     */
    OriginConfigurer removeRegex(String regex);
}
