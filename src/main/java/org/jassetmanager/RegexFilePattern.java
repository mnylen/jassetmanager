package org.jassetmanager;

import java.util.regex.Pattern;

public class RegexFilePattern implements FilePattern {
    private final Pattern regexPattern;

    public RegexFilePattern(String pattern) {
        this.regexPattern = Pattern.compile(pattern);
    }

    public RegexFilePattern(Pattern pattern) {
        this.regexPattern = pattern;
    }

    public boolean matches(String path) {
        return this.regexPattern.matcher(path).matches();
    }
}
