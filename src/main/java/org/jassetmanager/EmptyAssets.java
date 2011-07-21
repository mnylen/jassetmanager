package org.jassetmanager;

import java.util.Collections;
import java.util.Iterator;

public class EmptyAssets implements BundleAssets {
    public Iterator<Asset> getAssets() {
        return new EmptyIterator<Asset>();
    }

    public long getLastModified() {
        return -1L;
    }
}
