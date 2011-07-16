package org.jassetmanager;

public interface BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets)
            throws AssetException;
}
