package org.jassetmanager;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AssetFile {
    private final String path;
    private final URL url;
    
    public AssetFile(String path, ServletContext context) throws AssetException {
        this.path = path;

        try {
            this.url = context.getResource(this.path);
        } catch (MalformedURLException e) {
            throw new AssetException(
                    "Could not get URL for asset '" + path + "', because the asset path not a valid URL.", e);
        }
    }

    public String getPath() {
        return path;
    }

    public long getLastModified() throws AssetException {
        try {
            return this.url.openConnection().getLastModified();
        } catch (IOException e) {
            throw new AssetException("Could not get last modified time for '" + this.path + "'", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetFile assetFile = (AssetFile) o;

        if (path != null ? !path.equals(assetFile.path) : assetFile.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
