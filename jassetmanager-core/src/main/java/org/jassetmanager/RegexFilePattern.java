package org.jassetmanager;

import java.util.regex.Pattern;

/**
 * Matches asset context paths against a regular expression.
 *
 * @author Mikko Nylén
 */
public class RegexFilePattern implements FilePattern {
    private final Pattern regexPattern;

    /**
     * Initializes a new <code>RegexFilePattern</code> that matches any assets against the given
     * <code>Pattern</code>.
     *
     * <p>This is same as calling <code>new RegexFilePattern(Pattern.compile(pattern))</code></p>
     *
     * @param pattern the regex file pattern
     */
    public RegexFilePattern(String pattern) {
        this.regexPattern = Pattern.compile(pattern);
    }

    /**
     * Initializes a new <code>RegexFilePattern</code> that matches any assets against
     * the given <code>Pattern</code>.
     *
     * @param pattern the pattern to match against
     */
    public RegexFilePattern(Pattern pattern) {
        this.regexPattern = pattern;
    }

    public boolean matches(Asset asset) {
        return this.regexPattern.matcher(asset.getContextPath()).matches();
    }
}
