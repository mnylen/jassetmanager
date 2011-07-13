package org.jassetmanager;

import com.sun.org.apache.bcel.internal.generic.NEW;
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
    private ServletContext mockContext;
    private List<String> allFilePaths;
    private AssetBundle bundle;
    private AssetBundleConfiguration config;
    
    @Before
    public void setUp() {
        this.mockContext = mock(ServletContext.class);
        when(this.mockContext.getResourceAsStream("/css/reset.css")).thenReturn(
                new ByteArrayInputStream("html, body { margin: 0; }".getBytes()));

        when(this.mockContext.getResourceAsStream("/css/main.css")).thenReturn(
                new ByteArrayInputStream("body { background-color: #000; }".getBytes()));

        this.allFilePaths = new ArrayList<String>(Arrays.asList("/css/main.css", "/css/reset.css"));

        this.config = new AssetBundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/reset.css"))
                .addFilePattern(new RegexFilePattern("/css/main.css"));

        this.bundle = new AssetBundle(this.config, this.mockContext);
    }
    
    @Test
    public void testBuildsConcatenation() throws IOException {
        assertThat(this.bundle.isBuilt(), is(false));
        assertThat(this.bundle.getContent(), equalTo(new byte[0]));
        
        this.bundle.build(allFilePaths);
        
        assertThat(this.bundle.isBuilt(), is(true));
        assertThat(new String(this.bundle.getContent()), equalTo(
                "html, body { margin: 0; }\r\nbody { background-color: #000; }\r\n"));
    }
}
