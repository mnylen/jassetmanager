package org.jassetmanager;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class RegexFilePatternTest {
    @Test
    public void testMatchesPathAgainstTheRegex() {
        String matchingPath = "/css/buttons.css";
        String notMatchingPath = "/css/something.else";
        String regex = "/css/.+\\.css";

        FilePattern pattern = new RegexFilePattern(regex);
        assertThat(pattern.matches(matchingPath), is(true));
        assertThat(pattern.matches(notMatchingPath), is(false));
    }
}
