package org.jassetmanager;

import java.io.IOException;

public interface BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets)
            throws AssetException;

    public boolean isRebuildNeeded(Bundle bundle, BundleAssets assets)
            throws IOException;
}
