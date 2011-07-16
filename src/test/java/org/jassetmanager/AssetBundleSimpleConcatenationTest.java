package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class AssetBundleSimpleConcatenationTest {
    private List<AssetFile> allAssetFiles;
    private AssetBundle bundle;

    @Before
    public void setUp() throws Exception {
        ServletContext mockContext = mock(ServletContext.class);
        when(mockContext.getResourceAsStream("/css/reset.css")).thenReturn(
                new ByteArrayInputStream("html, body { margin: 0; }".getBytes()));

        when(mockContext.getResourceAsStream("/css/main.css")).thenReturn(
                new ByteArrayInputStream("body { background-color: #000; }".getBytes()));

        this.allAssetFiles = new ArrayList<AssetFile>(Arrays.asList(
                new AssetFile("/css/main.css", mockContext),
                new AssetFile("/css/reset.css", mockContext)));

        AssetBundleConfiguration config = new AssetBundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/reset.css"))
                .addFilePattern(new RegexFilePattern("/css/main.css"));

        this.bundle = new AssetBundle(config, mockContext);
    }
    
    @Test
    public void testBuildsConcatenation() throws AssetException {
        assertThat(this.bundle.isBuilt(), is(false));
        assertThat(this.bundle.getContent(), equalTo(new byte[0]));
        
        this.bundle.build(allAssetFiles);
        
        assertThat(this.bundle.isBuilt(), is(true));
        assertThat(new String(this.bundle.getContent()), equalTo(
                "html, body { margin: 0; }\r\nbody { background-color: #000; }\r\n"));
    }
}
