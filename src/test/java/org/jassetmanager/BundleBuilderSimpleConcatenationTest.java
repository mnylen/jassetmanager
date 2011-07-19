package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class BundleBuilderSimpleConcatenationTest {
    private Bundle bundle;
    private BundleBuilder builder;
    
    @Before
    public void setUp() throws Exception {
        FileSystem mockFs = mock(FileSystem.class);
        Asset resetCss = new Asset(mockFs, "/css/reset.css");
        Asset mainCss = new Asset(mockFs, "/css/main.css");

        when(mockFs.getAssets("/")).thenReturn(
                new HashSet<Asset>(Arrays.asList(
                        resetCss,
                        mainCss)));

        when(mockFs.getContent(resetCss)).thenReturn(
                "html, body { margin: 0; }".getBytes());

        when(mockFs.getContent(mainCss)).thenReturn(
                "body { background-color: #000; }".getBytes());

        AssetBundleConfiguration config = new AssetBundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/reset.css"))
                .addFilePattern(new RegexFilePattern("/css/main.css"));

        this.bundle = new Bundle();
        this.builder = new BundleBuilder(config, mockFs);
    }
    
    @Test
    public void testBuildsConcatenation() throws Exception {
        assertThat(this.bundle.isBuilt(), is(false));
        assertThat(this.bundle.getContent(), equalTo(new byte[0]));
        
        this.builder.build(this.bundle);

        assertThat(this.bundle.isBuilt(), is(true));
        assertThat(new String(this.bundle.getContent()), equalTo(
                "html, body { margin: 0; }\r\nbody { background-color: #000; }\r\n"));
    }
}
