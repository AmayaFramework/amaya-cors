package io.github.amayaframework.cors;

import java.util.Set;

/**
 *
 */
public interface OriginConfigurer extends AccessConfigurer<String> {

    /**
     *
     * @param values
     * @return
     */
    @Override
    OriginConfigurer allowed(Set<String> values);

    /**
     *
     * @return
     */
    @Override
    OriginConfigurer allowAny();

    /**
     *
     * @param value
     * @return
     */
    @Override
    OriginConfigurer allow(String value);

    /**
     *
     * @param values
     * @return
     */
    @Override
    OriginConfigurer allow(String... values);

    /**
     *
     * @param values
     * @return
     */
    @Override
    OriginConfigurer allow(Iterable<String> values);

    /**
     *
     * @return
     */
    @Override
    OriginConfigurer denyAny();

    /**
     *
     * @param value
     * @return
     */
    @Override
    OriginConfigurer deny(String value);

    /**
     *
     * @param values
     * @return
     */
    @Override
    OriginConfigurer deny(String... values);

    /**
     *
     * @param values
     * @return
     */
    @Override
    OriginConfigurer deny(Iterable<String> values);

    /**
     *
     * @return
     */
    Set<String> allowedRegexes();

    /**
     *
     * @param regexes
     * @return
     */
    OriginConfigurer allowedRegexes(Set<String> regexes);

    /**
     *
     * @param regex
     * @return
     */
    OriginConfigurer addRegex(String regex);

    /**
     *
     * @param regex
     * @return
     */
    OriginConfigurer removeRegex(String regex);
}
