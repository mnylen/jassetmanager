package org.jassetmanager;

import java.util.regex.Pattern;

public class BrowserMatchers {
    public static final UserAgentMatcher ANY = new UserAgentMatcher() {
        public boolean matches(String userAgent) {
            return true;
        };
    };

    public static final UserAgentMatcher MSIE = new UserAgentMatcher() {
        private final Pattern PATTERN = Pattern.compile("MSIE \\d\\.\\d");

        public boolean matches(String userAgent) {
            return PATTERN.matcher(userAgent).find();
        };
    };
}
