package org.jassetmanager;

import java.io.IOException;

public interface Manipulator {
    public void preManipulate(Asset asset, boolean isLast)
            throws AssetException, IOException;

    public void postManipulate(Bundle bundle)
            throws AssetException, IOException;
}
