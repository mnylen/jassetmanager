package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class AssetWalkerTest {
    private ServletContext context;
    
    @Before
    public void setUp() {
        this.context = mock(ServletContext.class);
        when(this.context.getResourcePaths("/")).thenReturn(
                new LinkedHashSet<String>(Arrays.asList("/js/", "/css/")));
        
        when(this.context.getResourcePaths("/js/")).thenReturn(
                new LinkedHashSet<String>(Arrays.asList("/js/lib/", "/js/application.js", "/js/fileupload.js")));

        when(this.context.getResourcePaths("/js/lib/")).thenReturn(
                new LinkedHashSet<String>(Arrays.asList("/js/lib/jquery.js")));

        when(this.context.getResourcePaths("/css/")).thenReturn(
                new LinkedHashSet<String>(Arrays.asList("/css/application.css", "/css/buttons.css")));
    }

    @Test
    public void testWalksAllAssetsRecursivelyInOrder() {
        final List<String> visitedPaths = new ArrayList<String>();
        AssetWalker.walkAssetTree(this.context, "/", new AssetVisitor() {
            public void visitAsset(String path) {
                visitedPaths.add(path);
            }
        });

        assertThat(visitedPaths.size(), equalTo(5));
        assertThat(visitedPaths.get(0), equalTo("/js/lib/jquery.js"));
        assertThat(visitedPaths.get(1), equalTo("/js/application.js"));
        assertThat(visitedPaths.get(2), equalTo("/js/fileupload.js"));
        assertThat(visitedPaths.get(3), equalTo("/css/application.css"));
        assertThat(visitedPaths.get(4), equalTo("/css/buttons.css"));
    }
}
