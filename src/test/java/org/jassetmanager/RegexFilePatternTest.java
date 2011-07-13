package org.jassetmanager;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class RegexFilePatternTest {
    @Test
    public void testMatchesPathAgainstTheRegex() {
        AssetFile matchingAssetFile = new AssetFile("/css/buttons.css");
        AssetFile notMatchingAssetFile = new AssetFile("/css/something.else");
        String regex = "/css/.+\\.css";

        FilePattern pattern = new RegexFilePattern(regex);
        assertThat(pattern.matches(matchingAssetFile), is(true));
        assertThat(pattern.matches(notMatchingAssetFile), is(false));
    }
}
