package org.jassetmanager;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AssetFile {
    private final String path;
    private final ServletContext context;
    private final URL url;
    
    public AssetFile(String path, ServletContext context) throws MalformedURLException {
        this.path = path;
        this.context = context;
        this.url = context.getResource(this.path);
    }

    public String getPath() {
        return path;
    }

    public long getLastModified() throws IOException {
        return this.url.openConnection().getLastModified();
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
