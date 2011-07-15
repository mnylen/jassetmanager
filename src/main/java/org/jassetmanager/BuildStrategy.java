package org.jassetmanager;

import javax.servlet.ServletContext;
import java.io.IOException;

public interface BuildStrategy {
    public boolean isRebuildNeeded(AssetBundle bundle, Assets assets)
            throws IOException;
}
