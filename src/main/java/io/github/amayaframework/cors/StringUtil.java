package io.github.amayaframework.cors;

import java.util.Locale;

final class StringUtil {
    private StringUtil() {
    }

    static <V> String render(Iterable<V> values) {
        if (values == null) {
            return null;
        }
        var iterator = values.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        var builder = new StringBuilder();
        builder.append(iterator.next());
        while (iterator.hasNext()) {
            builder.append(',').append(iterator.next());
        }
        return builder.toString().toLowerCase(Locale.ENGLISH);
    }

    @SafeVarargs
    static <V> String render(V... values) {
        if (values == null) {
            return null;
        }
        var length = values.length;
        if (length == 0) {
            return null;
        }
        var builder = new StringBuilder();
        builder.append(values[0]);
        for (var i = 1; i < length; ++i) {
            builder.append(',').append(values[i]);
        }
        return builder.toString().toLowerCase(Locale.ENGLISH);
    }
}
