package org.jassetmanager;

public interface Manipulator {
    public byte[] manipulate(AssetBundle bundle, String filePath, byte[] content, boolean isLast);
}
