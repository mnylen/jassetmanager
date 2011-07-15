package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

public class RebuildIfModifiedBuildStrategyTest {
    private Assets mockAssets;
    private AssetBundle mockBundle;
    private BuildStrategy strategy;

    @Before
    public void setUp() {
        this.mockAssets = mock(Assets.class);
        this.mockBundle = mock(AssetBundle.class);
        this.strategy = new RebuildIfModifiedStrategy();
    }

    @Test
    public void testRebuildNeededWhenBundleNotBuilt() throws Exception {
        when(this.mockBundle.isBuilt()).thenReturn(false);
        assertThat(this.strategy.isRebuildNeeded(this.mockBundle, this.mockAssets), is(true));
    }

    @Test
    public void testRebuildNotNeededIfBundleAssetsHaveNotBeenModifiedSinceLastBuild() throws Exception {
        long now = System.currentTimeMillis();

        when(this.mockBundle.isBuilt()).thenReturn(true);
        when(this.mockBundle.getBuiltAt()).thenReturn(now);
        when(this.mockBundle.getLastModified(this.mockAssets)).thenReturn(now-300);

        assertThat(this.strategy.isRebuildNeeded(this.mockBundle, this.mockAssets), is(false));
    }

    @Test
    public void testRebuildNeededIfBundleAssetsHaveBeenModifiedSinceLastBuild() throws Exception {
        long now = System.currentTimeMillis();

        when(this.mockBundle.isBuilt()).thenReturn(true);
        when(this.mockBundle.getBuiltAt()).thenReturn(now);
        when(this.mockBundle.getLastModified(this.mockAssets)).thenReturn(now+300);

        assertThat(this.strategy.isRebuildNeeded(this.mockBundle, this.mockAssets), is(true));
    }
}
