package io.github.amayaframework.cors;

import java.util.*;
import java.util.regex.Pattern;

final class CompileOriginBuilder extends AbstractOriginConfigurer<OriginConfigurer> {

    private static List<Pattern> compileRegexes(Collection<String> regexes) {
        var ret = new ArrayList<Pattern>(regexes.size());
        for (var raw : regexes) {
            ret.add(Pattern.compile(raw));
        }
        return ret;
    }

    List<Pattern> buildRegexes() {
        if (regexes == null) {
            return null;
        }
        var regexes = this.regexes;
        this.regexes = null;
        return Collections.unmodifiableList(compileRegexes(regexes));
    }

    Set<String> buildStrict() {
        if (allowed == null) {
            return null;
        }
        var allowed = this.allowed;
        this.allowed = null;
        return Collections.unmodifiableSet(Util.toLowerCase(allowed));
    }
}
