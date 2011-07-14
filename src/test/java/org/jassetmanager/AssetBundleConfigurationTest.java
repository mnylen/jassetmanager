package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;

public class AssetBundleConfigurationTest {
    private ServletContext mockContext;

    @Before
    public void setUp() {
        this.mockContext = mock(ServletContext.class);
    }

    @Test
    public void testGetContentPosition() throws Exception {

        AssetBundleConfiguration config = new AssetBundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/.+\\.css"))
                .addFilePattern(new RegexFilePattern("/css/.+\\.less"));

        assertThat(config.getContentPosition(new AssetFile("/css/buttons.css", this.mockContext)), is(0));
        assertThat(config.getContentPosition(new AssetFile("/css/buttons.less", this.mockContext)), is(1));
        assertThat(config.getContentPosition(new AssetFile("/css/something.else", this.mockContext)), is(-1));
    }

    @Test
    public void testConstructorWithBaseConfigurationIncludesBaseConfiguration() throws Exception {
        AssetBundleConfiguration baseConfiguration = new AssetBundleConfiguration()
                .addFilePattern("pattern1")
                .addFilePattern("pattern2");

        AssetBundleConfiguration derivedConfiguration = new AssetBundleConfiguration(baseConfiguration)
                .addFilePattern("pattern3");

        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern1", this.mockContext)), is(0));
        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern2", this.mockContext)), is(1));
        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern3", this.mockContext)), is(2));
    }

    @Test
    public void testBaseConfigurationFilePatternChangesDontAffectDerivedConfiguration() throws Exception {
                AssetBundleConfiguration baseConfiguration = new AssetBundleConfiguration()
                .addFilePattern("pattern1")
                .addFilePattern("pattern2");

        AssetBundleConfiguration derivedConfiguration = new AssetBundleConfiguration(baseConfiguration)
                .addFilePattern("pattern3");

        baseConfiguration.addFilePattern("pattern4");
        assertThat(derivedConfiguration.getContentPosition(new AssetFile("pattern4", this.mockContext)), is(-1));
    }
}
