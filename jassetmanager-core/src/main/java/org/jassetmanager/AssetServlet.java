package org.jassetmanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AssetServlet extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(AssetServlet.class);
    private final BundleRegistry registry;
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
    protected static final String ASSET_ROOT_PATH = "/";
    private boolean debug;

    public AssetServlet() {
        this.registry = new BundleRegistry();
        this.debug = false;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String userAgent = request.getHeader(USER_AGENT_HEADER);
        if (userAgent == null) {
            userAgent = "";
        }

        String uri = request.getRequestURI();
        BundleRegistry.RegistryEntry registryEntry = this.registry.get(uri, userAgent);
        if (registryEntry == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            Bundle bundle = registryEntry.getBundle();

            try {
                if (!(registryEntry.isPrebuilt())) {
                    registryEntry.getBuilder().build(bundle);
                }
            } catch (AssetException e) {
                handleException(uri, userAgent, response, e);
                return;
            } catch (IOException e) {
                handleException(uri, userAgent, response, e);
                return;
            }

            if (!(isBundleModified(bundle, request))) {
                LOG.debug("Bundle for '" + uri + "' and User-Agent '" + userAgent + "' not modified.");
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            } else {
                LOG.debug("Serving bundle for '" + uri + "' and User-Agent '" + userAgent + "'");
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(registryEntry.getServeAsMimeType());
                response.setContentLength(bundle.getContent().length);
                response.getOutputStream().write(bundle.getContent());
            }
        }
    }

    protected void handleException(String uri,
                                   String userAgent,
                                   HttpServletResponse response,
                                   Exception e) throws IOException {

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        if (this.debug) {
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }

        LOG.error("Failed to serve asset bundle for '" + uri + "' and UserAgent '" + userAgent + "'", e);
    }
    
    protected boolean isBundleModified(Bundle bundle, HttpServletRequest request) {
        long ifModifiedSince = request.getDateHeader(IF_MODIFIED_SINCE_HEADER);

        return ifModifiedSince <= 0 || bundle.getUpdatedAt() > ifModifiedSince;
    }

    protected void configureBundle(String requestPath,
                                   String serveAsMimeType,
                                   UserAgentMatcher userAgentMatcher,
                                   BundleConfiguration config) {
        
        this.registry.register(
                requestPath,
                serveAsMimeType,
                userAgentMatcher,
                new Bundle(),
                new BundleBuilder(config, new ServletContextFileSystem(this.getServletContext())));
    }

    protected void configureBundle(String requestPath,
                                   String serveAsMimeType,
                                   UserAgentMatcher userAgentMatcher,
                                   PrebuiltBundle bundle) {

        this.registry.register(
                requestPath,
                serveAsMimeType,
                userAgentMatcher,
                bundle,
                null);
    }
}
