package io.github.amayaframework.cors;

import java.util.Set;

public interface OriginConfigurer extends AccessConfigurer<String> {

    @Override
    OriginConfigurer allowed(Set<String> values);

    @Override
    OriginConfigurer allowAny();

    @Override
    OriginConfigurer allow(String value);

    @Override
    OriginConfigurer allow(String... values);

    @Override
    OriginConfigurer allow(Iterable<String> values);

    @Override
    OriginConfigurer denyAny();

    @Override
    OriginConfigurer deny(String value);

    @Override
    OriginConfigurer deny(String... values);

    @Override
    OriginConfigurer deny(Iterable<String> values);

    Set<String> allowedRegexes();

    OriginConfigurer allowedRegexes(Set<String> regexes);

    OriginConfigurer addRegex(String regex);

    OriginConfigurer removeRegex(String regex);
}
