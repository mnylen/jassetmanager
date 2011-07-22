package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

public class FileSystemBundleAssetsTest {
    private FileSystemBundleAssets assets;

    @Before
    public void setUp() throws Exception {
        FileSystem mockFs = mock(FileSystem.class);
        when(mockFs.getAssets("/")).thenReturn(
                new HashSet<Asset>(Arrays.asList(
                    new Asset(mockFs, "/css/buttons.css"),
                    new Asset(mockFs, "/css/reset.css"),
                    new Asset(mockFs, "/js/jquery.js"),
                    new Asset(mockFs, "/css/application.css")
                )));

        this.assets = new FileSystemBundleAssets(mockFs,  "/",
                Arrays.asList(
                    (FilePattern)new RegexFilePattern("/css/reset.css"),
                    new RegexFilePattern("/css/application.css"),
                    new RegexFilePattern("/css/buttons.css")
                ));
    }

    @Test
    public void testGetAssets() throws Exception {
        Iterator<Asset> assets = this.assets.getAssets();

        assertThat(assets.hasNext(), is(true));
        assertThat(assets.next().getContextPath(), equalTo("/css/reset.css"));
        assertThat(assets.hasNext(), is(true));
        assertThat(assets.next().getContextPath(), equalTo("/css/application.css"));
        assertThat(assets.hasNext(), is(true));
        assertThat(assets.next().getContextPath(), equalTo("/css/buttons.css"));
        assertThat(assets.hasNext(), is(false));

        try {
            assets.next();
            fail("Should have raised NoSuchElementException");
        } catch (NoSuchElementException e) {
            
        }
    }
    
}
