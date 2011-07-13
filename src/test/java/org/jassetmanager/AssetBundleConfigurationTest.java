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

        assertThat(config.getContentPosition(new AssetFile("/css/buttons.css")), is(0));
        assertThat(config.getContentPosition(new AssetFile("/css/buttons.less")), is(1));
        assertThat(config.getContentPosition(new AssetFile("/css/something.else")), is(-1));
    }

    @Test
    public void testConstructorWithBaseConfigurationIncludesBaseConfiguration() {
        AssetBundleConfiguration baseConfiguration = new AssetBundleConfiguration()
                .addFilePattern("pattern1")
                .addFilePattern("pattern2");

        AssetBundleConfiguration derivedConfiguration = new AssetBundleConfiguration(baseConfiguration)
                .addFilePattern("pattern3");

        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern1")), is(0));
        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern2")), is(1));
        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern3")), is(2));
    }

    @Test
    public void testBaseConfigurationFilePatternChangesDontAffectDerivedConfiguration() {
                AssetBundleConfiguration baseConfiguration = new AssetBundleConfiguration()
                .addFilePattern("pattern1")
                .addFilePattern("pattern2");

        AssetBundleConfiguration derivedConfiguration = new AssetBundleConfiguration(baseConfiguration)
                .addFilePattern("pattern3");

        baseConfiguration.addFilePattern("pattern4");
        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern4")), is(-1));
    }
}
