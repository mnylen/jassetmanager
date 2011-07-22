package org.jassetmanager;

import java.io.IOException;

public interface BuildStrategy {
    public boolean isRebuildNeeded(Bundle bundle, BundleAssets assets)
            throws IOException;
}
