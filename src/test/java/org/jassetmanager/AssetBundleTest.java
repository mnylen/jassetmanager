package org.jassetmanager;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class AssetBundleTest {

    @Test
    public void testIncludesFilesThatMatchAnyAddedPattern() {
        AssetBundle bundle = new AssetBundle()
                .addFilePattern(new RegexFilePattern("/css/.+\\.css"))
                .addFilePattern(new RegexFilePattern("/css/.+\\.less"));

        assertThat(bundle.includesFile("/css/buttons.css"), is(true));
        assertThat(bundle.includesFile("/css/buttons.less"), is(true));
        assertThat(bundle.includesFile("/css/something.else"), is(false));
    }

}
