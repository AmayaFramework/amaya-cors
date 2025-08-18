package io.github.amayaframework.cors;

import java.util.*;
import java.util.stream.Collectors;

final class Util {
    private Util() {
    }

    static <V> void allow(Set<V> set, V[] values) {
        if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.add(value);
            }
        }
    }

    static <V> void allow(Set<V> set, Iterable<V> values) {
        if (values instanceof Collection) {
            set.addAll((Collection<V>) values);
            set.removeIf(Objects::isNull);
        } else if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.add(value);
            }
        }
    }

    static <V> void deny(Set<V> set, V[] values) {
        if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.remove(value);
            }
        }
    }

    static <V> void deny(Set<V> set, Iterable<V> values) {
        if (values instanceof Collection) {
            set.removeAll((Collection<V>) values);
        } else if (values != null) {
            for (var value : values) {
                if (value == null) {
                    continue;
                }
                set.remove(value);
            }
        }
    }

    static Set<String> toLowerCase(Set<String> set) {
        return set.stream().map(h -> h.toLowerCase(Locale.ENGLISH)).collect(Collectors.toSet());
    }
}
