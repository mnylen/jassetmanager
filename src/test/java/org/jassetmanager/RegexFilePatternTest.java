package org.jassetmanager;

import org.junit.Test;

import javax.servlet.ServletContext;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;

public class RegexFilePatternTest {
    @Test
    public void testMatchesPathAgainstTheRegex() throws Exception {
        ServletContext mockContext = mock(ServletContext.class);
        AssetFile matchingAssetFile = new AssetFile("/css/buttons.css", mockContext);
        AssetFile notMatchingAssetFile = new AssetFile("/css/something.else", mockContext);
        String regex = "/css/.+\\.css";

        FilePattern pattern = new RegexFilePattern(regex);
        assertThat(pattern.matches(matchingAssetFile), is(true));
        assertThat(pattern.matches(notMatchingAssetFile), is(false));
    }
}
