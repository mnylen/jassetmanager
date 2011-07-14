package org.jassetmanager;

import com.sun.istack.internal.Nullable;

public interface Manipulator {
    public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content);
}
