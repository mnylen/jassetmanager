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
    
}
