package org.jassetmanager;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class AssetBundleConfigurationTest {

    @Test
    public void testGetContentPosition() {
        AssetBundleConfiguration config = new AssetBundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/.+\\.css"))
                .addFilePattern(new RegexFilePattern("/css/.+\\.less"));

        assertThat(config.getContentPosition("/css/buttons.css"), is(0));
        assertThat(config.getContentPosition("/css/buttons.less"), is(1));
        assertThat(config.getContentPosition("/css/something.else"), is(-1));
    }

    @Test
    public void testConstructorWithBaseConfigurationIncludesFilePatternsFromBase() {
        AssetBundleConfiguration baseConfiguration = new AssetBundleConfiguration()
                .addFilePattern("pattern1")
                .addFilePattern("pattern2");

        AssetBundleConfiguration derivedConfiguration = new AssetBundleConfiguration(baseConfiguration)
                .addFilePattern("pattern3");

        assertThat(derivedConfiguration.getContentPosition("pattern1"), is(0));
        assertThat(derivedConfiguration.getContentPosition("pattern2"), is(1));
        assertThat(derivedConfiguration.getContentPosition("pattern3"), is(2));
    }

    @Test
    public void testBaseConfigurationFilePatternChangesDontAffectDerivedConfiguration() {
                AssetBundleConfiguration baseConfiguration = new AssetBundleConfiguration()
                .addFilePattern("pattern1")
                .addFilePattern("pattern2");

        AssetBundleConfiguration derivedConfiguration = new AssetBundleConfiguration(baseConfiguration)
                .addFilePattern("pattern3");

        baseConfiguration.addFilePattern("pattern4");
        assertThat(derivedConfiguration.getContentPosition("pattern4"), is(-1));
    }
}
