package org.jassetmanager;

import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import java.util.Map;

public class RequestUtil {
    public static HttpTester getResponse(ServletTester tester, String uri) throws Exception {
        HttpTester request = new HttpTester();
        request.setMethod("GET");
        request.setHeader("Host", "tester");
        request.setVersion("HTTP/1.1");
        request.setURI(uri);
        
        HttpTester response = new HttpTester();
        response.parse(tester.getResponses(request.generate()));

        return response;
    }
}
