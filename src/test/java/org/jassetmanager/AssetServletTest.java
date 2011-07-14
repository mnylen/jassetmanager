package org.jassetmanager;

import org.jassetmanager.testservlets.SimpleAssetConcatenationServlet;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class AssetServletTest {
    private ServletTester tester;


    @Before
    public void setUp() throws Exception {
        this.tester = new ServletTester();
        this.tester.setClassLoader(AssetServlet.class.getClassLoader());
        this.tester.setContextPath("/");
        this.tester.setResourceBase("src/test/resources");
        this.tester.addServlet(SimpleAssetConcatenationServlet.class, "*.css");
        this.tester.start();
    }

    @Test
    public void testBuildsAndOutputsBundleContent() throws Exception {
        HttpTester request = new HttpTester();
        request.setMethod("GET");
        request.setHeader("Host", "tester");
        request.setVersion("HTTP/1.1");
        request.setURI("/css/application.css");

        HttpTester response = new HttpTester();
        response.parse(tester.getResponses(request.generate()));

        assertThat(response.getContent(),
                equalTo("html, body { margin: 0; }\r\nbody { background-color: #000; }\r\n"));
    }
}