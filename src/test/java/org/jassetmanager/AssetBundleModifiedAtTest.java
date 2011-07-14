package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;
import javax.servlet.ServletContext;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

public class AssetBundleModifiedAtTest {
    private ServletContext mockContext;

    @Before
    public void setUp() {
        this.mockContext = mock(ServletContext.class);
        
    }

    @Test
    public void testReturnsLargestTimestampOfAllFiles() {
        
    }
}
