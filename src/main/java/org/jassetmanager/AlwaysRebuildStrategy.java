package org.jassetmanager;

public class AlwaysRebuildStrategy implements BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets) {
        return true;
    }
}
