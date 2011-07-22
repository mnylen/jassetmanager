package org.jassetmanager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

public class RebuildIfModifiedBuildStrategyTest {
    private BundleAssets mockBundleAssets;
    private Bundle mockBundle;
    private BuildStrategy strategy;

    @Before
    public void setUp() {
        this.mockBundleAssets = mock(BundleAssets.class);
        this.mockBundle = mock(Bundle.class);
        this.strategy = new RebuildIfModifiedStrategy();
    }

    @Test
    public void testRebuildNeededWhenBundleNotBuilt() throws Exception {
        when(this.mockBundle.isBuilt()).thenReturn(false);
        assertThat(this.strategy.isRebuildNeeded(this.mockBundle, this.mockBundleAssets), is(true));
    }

    @Test
    public void testRebuildNotNeededIfBundleAssetsHaveNotBeenModifiedSinceLastBuild() throws Exception {
        long now = System.currentTimeMillis();

        when(this.mockBundle.isBuilt()).thenReturn(true);
        when(this.mockBundle.getUpdatedAt()).thenReturn(now);
        when(this.mockBundleAssets.getLastModified()).thenReturn(now-300);

        assertThat(this.strategy.isRebuildNeeded(this.mockBundle, this.mockBundleAssets), is(false));
    }

    @Test
    public void testRebuildNeededIfBundleAssetsHaveBeenModifiedSinceLastBuild() throws Exception {
        long now = System.currentTimeMillis();

        when(this.mockBundle.isBuilt()).thenReturn(true);
        when(this.mockBundle.getUpdatedAt()).thenReturn(now);
        when(this.mockBundleAssets.getLastModified()).thenReturn(now+300);

        assertThat(this.strategy.isRebuildNeeded(this.mockBundle, this.mockBundleAssets), is(true));
    }
}
