package org.jassetmanager;

import java.io.IOException;
import java.util.Set;

public interface FileSystem {
    public Set<Asset> getAssets(String root) throws IOException;
    public long getLastModified(Asset asset) throws IOException;
    public byte[] getContent(Asset asset) throws IOException;
}
