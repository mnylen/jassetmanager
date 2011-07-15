package org.jassetmanager;

public class BuildOnceStrategy implements BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets) {
        return !(bundle.isBuilt());
    }
}
