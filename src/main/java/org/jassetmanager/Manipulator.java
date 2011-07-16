package org.jassetmanager;

public interface Manipulator {
    public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content)
            throws AssetException;
}
