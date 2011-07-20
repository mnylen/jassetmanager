package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ServletContextFileSystemTest {
    private ServletContextFileSystem fs;
    
    @Before
    public void setUp() {
        ServletContext mockContext = mock(ServletContext.class);
        when(mockContext.getResourcePaths("/")).thenReturn(
                new HashSet<String>(Arrays.asList("/css/", "/js/")));

        when(mockContext.getResourcePaths("/css/")).thenReturn(
                new HashSet<String>(Arrays.asList("/css/reset.css", "/css/buttons.css")));

        when(mockContext.getResourcePaths("/js/")).thenReturn(
                new HashSet<String>(Arrays.asList("/js/jquery.js")));

        this.fs = new ServletContextFileSystem(mockContext);
    }

    @Test
    public void testGetAssets() throws Exception {
        Set<Asset> assets = fs.getAssets("/");
        Set<String> assetContextPaths = new HashSet<String>();
        for (Asset asset : assets) {
            assetContextPaths.add(asset.getContextPath());
        }

        assertThat(assetContextPaths.size(), is(3));
        assertThat(assetContextPaths.containsAll(Arrays.asList(
                "/css/reset.css",
                "/css/buttons.css",
                "/js/jquery.js")), is(true));
    }
}
