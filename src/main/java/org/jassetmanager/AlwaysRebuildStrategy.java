package org.jassetmanager;

import java.io.IOException;

public class AlwaysRebuildStrategy implements BuildStrategy {
    public boolean isRebuildNeeded(Bundle bundle, BundleAssets assets) throws IOException {
        return true;
    }
}
