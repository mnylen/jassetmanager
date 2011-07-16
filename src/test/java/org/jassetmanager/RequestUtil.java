package org.jassetmanager;

import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;

import java.util.Map;

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
}
