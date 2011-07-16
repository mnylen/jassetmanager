package org.jassetmanager;

import com.sun.istack.internal.NotNull;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class AssetFileWalkerTest {
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
    public void testWalksAllAssetsRecursivelyInOrder() throws Exception {
        final List<AssetFile> visitedAssetFiles = new ArrayList<AssetFile>();
        AssetFileWalker.walkAssetFiles(this.context, "/", new AssetFileVisitor() {
            public void visitFile(@NotNull AssetFile assetFile) {
                visitedAssetFiles.add(assetFile);
            }
        });

        assertThat(visitedAssetFiles.size(), equalTo(5));
        assertThat(visitedAssetFiles.get(0), equalTo(new AssetFile("/js/lib/jquery.js", this.context)));
        assertThat(visitedAssetFiles.get(1), equalTo(new AssetFile("/js/application.js", this.context)));
        assertThat(visitedAssetFiles.get(2), equalTo(new AssetFile("/js/fileupload.js", this.context)));
        assertThat(visitedAssetFiles.get(3), equalTo(new AssetFile("/css/application.css", this.context)));
        assertThat(visitedAssetFiles.get(4), equalTo(new AssetFile("/css/buttons.css", this.context)));
    }

    @Test
    public void testDoesNotTryToWalkNullPaths() throws AssetException {
        when(this.context.getResourcePaths("/img/")).thenReturn(null);
        AssetFileWalker.walkAssetFiles(this.context, "/img/", new AssetFileVisitor() {
            public void visitFile(@NotNull AssetFile assetFile) {
                fail("Visited null");
            }
        });
    }
}
