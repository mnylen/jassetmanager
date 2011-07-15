package org.jassetmanager;

import java.io.IOException;

public class RebuildIfModifiedStrategy implements BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets) throws IOException {
        return !(bundle.isBuilt()) || bundle.getLastModified(assets) > bundle.getBuiltAt();
    }
}
