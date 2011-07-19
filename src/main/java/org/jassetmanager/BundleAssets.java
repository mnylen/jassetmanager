package org.jassetmanager;

import java.io.IOException;
import java.util.Iterator;

public interface BundleAssets {
    public Iterator<Asset> getAssets() throws IOException;
    public long getLastModified() throws IOException;
}
