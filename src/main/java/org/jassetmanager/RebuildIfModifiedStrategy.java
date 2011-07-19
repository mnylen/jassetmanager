package org.jassetmanager;

import java.io.IOException;

public class RebuildIfModifiedStrategy implements BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets) throws AssetException {
        return !(bundle.isBuilt()) || bundle.getLastModified(assets) > bundle.getBuiltAt();
    }

    public boolean isRebuildNeeded(Bundle bundle, BundleAssets assets) throws IOException {
        return !(bundle.isBuilt()) || assets.getLastModified() > bundle.getUpdatedAt();
    }
}
