package org.jassetmanager;

import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.jassetmanager.testservlets.SimpleAssetConcatenationServlet;

import javax.servlet.http.HttpServlet;

public class RequestUtil {
    public static HttpTester getResponse(ServletTester tester, String uri) throws Exception {
        return getResponse(tester, uri, 0L);
    }

    public static HttpTester getResponse(ServletTester tester, String uri, long ifModifiedSince) throws Exception {
        HttpTester request = new HttpTester();
        request.setMethod("GET");
        request.setHeader("Host", "tester");
        request.setVersion("HTTP/1.1");
        request.setURI(uri);

        if (ifModifiedSince > 0) {
            request.addDateHeader("If-Modified-Since", ifModifiedSince);
        }

        HttpTester response = new HttpTester();
        response.parse(tester.getResponses(request.generate()));

        return response;
    }

    public static ServletTester createAndStartServletTester(Class<? extends HttpServlet> servletClass, String urlPattern)
        throws Exception {
        
        ServletTester tester = new ServletTester();
        tester.setClassLoader(servletClass.getClassLoader());
        tester.setContextPath("/");
        tester.setResourceBase("src/test/resources");
        tester.addServlet(servletClass, urlPattern);
        tester.start();

        return tester;
    }
}
