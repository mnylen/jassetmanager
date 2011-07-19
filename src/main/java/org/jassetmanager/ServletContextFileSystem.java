package org.jassetmanager;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ServletContextFileSystem implements FileSystem {
    private final ServletContext context;

    public ServletContextFileSystem(ServletContext context) {
        this.context = context;
    }
    
    public Set<Asset> getAssets(String root) throws IOException {
        final Set<Asset> assets = new HashSet<Asset>();
        listAssets(root, assets);
        
        return assets;
    }

    private void listAssets(String dir, Set<Asset> assets) {
        @SuppressWarnings("unchecked")
        Set<String> contextPaths = this.context.getResourcePaths(dir);

        for (String contextPath : contextPaths) {
            if (contextPath.charAt(contextPath.length()-1) == '/') {
                listAssets(contextPath, assets);
            } else {
                assets.add(new Asset(this, contextPath));
            }
        }
    }

    public long getLastModified(Asset asset) throws IOException {
        URL url = this.context.getResource(asset.getContextPath());
        if (url == null) {
            throw new AssetNotFoundException(asset.getContextPath());
        }

        return url.openConnection().getLastModified();
    }

    public byte[] getContent(Asset asset) throws IOException {
        return ResourceUtil.readInputStream(
                this.context.getResourceAsStream(
                        asset.getContextPath()));
    }
}
