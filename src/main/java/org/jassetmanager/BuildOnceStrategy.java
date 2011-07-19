package org.jassetmanager;

import java.io.IOException;

public class BuildOnceStrategy implements BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets) {
        return !(bundle.isBuilt());
    }

    public boolean isRebuildNeeded(Bundle bundle, BundleAssets assets) throws IOException {
        return !(bundle.isBuilt());
    }
}
