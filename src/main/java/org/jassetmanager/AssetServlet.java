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
    private static final String ASSET_ROOT_PATH = "/";

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
            if (isBundleRebuildNeeded(bundle)) {
                bundle.build(findAllAssetPaths());
            }

            if (isBundleModified(bundle, request)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(registryEntry.getServeAsMimeType());
                response.setContentLength(bundle.getContent().length);
                response.getOutputStream().write(bundle.getContent());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }
    }

    protected boolean isBundleModified(AssetBundle bundle, HttpServletRequest request) {
        return true;
    }

    protected boolean isBundleRebuildNeeded(AssetBundle bundle) {
        if (!(bundle.isBuilt())) {
            return true;
        } else {
            if (!(cache)) {
                return true;
            }
        }

        return false;
    }

    protected List<AssetFile> findAllAssetPaths() {
        final List<AssetFile> allAssetAssetFiles = new ArrayList<AssetFile>();
        AssetFileWalker.walkAssetFiles(this.getServletContext(), ASSET_ROOT_PATH, new AssetFileVisitor() {
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
