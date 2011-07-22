package org.jassetmanager;

import org.junit.Test;

import javax.servlet.ServletContext;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;

public class RegexFilePatternTest {
    @Test
    public void testMatchesPathAgainstTheRegex() throws Exception {
        FileSystem mockFs = mock(FileSystem.class);
        Asset matchingAssetFile = new Asset(mockFs, "/css/buttons.css");
        Asset notMatchingAssetFile = new Asset(mockFs, "/css/something.else");
        String regex = "/css/.+\\.css";

        FilePattern pattern = new RegexFilePattern(regex);
        assertThat(pattern.matches(matchingAssetFile), is(true));
        assertThat(pattern.matches(notMatchingAssetFile), is(false));
    }
}
