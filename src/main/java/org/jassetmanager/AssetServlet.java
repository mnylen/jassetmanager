package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AssetServlet extends HttpServlet {
    private final AssetRegistry registry;
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
    protected static final String ASSET_ROOT_PATH = "/";
    private boolean debug;

    public AssetServlet() {
        this.registry = new AssetRegistry();
        this.debug = false;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
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
            try {
                rebuildBundleIfNeeded(bundle);
            } catch (AssetException e) {
                handleException(request, response, e);
            }

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

    protected void handleException(HttpServletRequest request,
                                   HttpServletResponse response,
                                   AssetException e) throws IOException {

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        if (this.debug) {
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

    protected void rebuildBundleIfNeeded(AssetBundle bundle) throws AssetException {
        Assets assets = new Assets(this.getServletContext(), bundle.getConfiguration().getContextRootPath());
        if (bundle.getConfiguration().getBuildStrategy().isRebuildNeeded(bundle, assets)) {
            bundle.build(assets.listAssets());
        }
    }
    
    protected boolean isBundleModified(AssetBundle bundle, HttpServletRequest request) {
        long ifModifiedSince = request.getDateHeader(IF_MODIFIED_SINCE_HEADER);
        if (ifModifiedSince > 0) {
            return bundle.getBuiltAt() > ifModifiedSince;
        }

        return true;
    }

    protected void configureBundle(String requestPath, String serveAsMimeType, UserAgentMatcher userAgentMatcher, AssetBundleConfiguration config) {
        this.registry.register(requestPath, serveAsMimeType, userAgentMatcher,
                new AssetBundle(config, this.getServletContext()));
    }
}
