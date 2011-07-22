package org.jassetmanager;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class BundleConfigurationTest {

    @Test
    public void testConstructorWithBaseConfigurationIncludesBaseConfiguration() throws Exception {
        FilePattern pattern1 = new RegexFilePattern("pattern1");
        FilePattern pattern2 = new RegexFilePattern("pattern2");
        FilePattern pattern3 = new RegexFilePattern("pattern3");
        
        BundleConfiguration baseConfiguration = new BundleConfiguration()
                .addFilePattern(pattern1)
                .addFilePattern(pattern2);

        BundleConfiguration derivedConfiguration = new BundleConfiguration(baseConfiguration)
                .addFilePattern(pattern3);

        assertThat(derivedConfiguration.getFilePatterns().get(0), is(pattern1));
        assertThat(derivedConfiguration.getFilePatterns().get(1), is(pattern2));
        assertThat(derivedConfiguration.getFilePatterns().get(2), is(pattern3));
    }

    @Test
    public void testBaseConfigurationFilePatternChangesDontAffectDerivedConfiguration() throws Exception {
        FilePattern pattern1 = new RegexFilePattern("pattern1");
        FilePattern pattern2 = new RegexFilePattern("pattern2");
        FilePattern pattern3 = new RegexFilePattern("pattern3");

        BundleConfiguration baseConfiguration = new BundleConfiguration()
                .addFilePattern(pattern1)
                .addFilePattern(pattern2);

        BundleConfiguration derivedConfiguration = new BundleConfiguration(baseConfiguration)
                .addFilePattern(pattern3);

        baseConfiguration.addFilePattern(new RegexFilePattern("pattern4"));

        assertThat(derivedConfiguration.getFilePatterns().size(), is(3));
    }
}