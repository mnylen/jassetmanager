package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssetServlet extends HttpServlet {
    private final AssetRegistry registry;
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
    protected static final String ASSET_ROOT_PATH = "/";

    private boolean cache;

    public AssetServlet() {
        this.registry = new AssetRegistry();
        this.cache = true;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isCacheEnabled() {
        return cache;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userAgent = request.getHeader(USER_AGENT_HEADER);
        if (userAgent == null) {
            userAgent = "";
        }

        String requestPath = request.getRequestURI();
        AssetRegistry.RegistryEntry registryEntry = this.registry.get(requestPath, userAgent);
        if (registryEntry == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            AssetBundle bundle = registryEntry.getBundle();
            rebuildBundleIfNeeded(bundle);

            if (!(isBundleModified(bundle, request))) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(registryEntry.getServeAsMimeType());
                response.setContentLength(bundle.getContent().length);
                response.getOutputStream().write(bundle.getContent());
            }
        }
    }

    protected void rebuildBundleIfNeeded(AssetBundle bundle) throws IOException {
        if (cache && bundle.isBuilt()) {
            return;
        } else {
            List<AssetFile> allAssetFiles = findAllAssetFilesInContext(
                    bundle.getConfiguration().getContextRootPath());

            if (!(bundle.isBuilt())) {
                bundle.build(allAssetFiles);
            } else {
                long lastModifiedAt = bundle.getLastModified(allAssetFiles);
                if (lastModifiedAt > bundle.getBuiltAt()) {
                    bundle.build(allAssetFiles);
                }
            }
        }
    }
    
    protected boolean isBundleModified(AssetBundle bundle, HttpServletRequest request)
        throws IOException {

        long ifModifiedSince = request.getDateHeader(IF_MODIFIED_SINCE_HEADER);
        if (ifModifiedSince > 0) {
            return bundle.getBuiltAt() > ifModifiedSince;
        }

        return true;
    }

    protected List<AssetFile> findAllAssetFilesInContext(String rootPath) {
        final List<AssetFile> allAssetAssetFiles = new ArrayList<AssetFile>();
        AssetFileWalker.walkAssetFiles(this.getServletContext(), rootPath, new AssetFileVisitor() {
            public void visitFile(@NotNull AssetFile assetFile) {
                allAssetAssetFiles.add(assetFile);
            }
        });

        return allAssetAssetFiles;
    }

    protected void configureBundle(String requestPath, String serveAsMimeType, UserAgentMatcher userAgentMatcher, AssetBundleConfiguration config) {
        this.registry.register(requestPath, serveAsMimeType, userAgentMatcher,
                new AssetBundle(config, this.getServletContext()));
    }
}
