package org.jassetmanager;

import org.jassetmanager.testservlets.SimpleAssetConcatenationServlet;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;

import javax.servlet.http.HttpServletResponse;

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
        HttpTester response = RequestUtil.getResponse(tester, "/css/application.css");

        assertThat(response.getContent(),
                equalTo("html, body { margin: 0; }\r\nbody { background-color: #000; }\r\n"));
    }
    
    @Test
    public void testRespondsWithNotModifiedWhenTheBundleHasNotBeenModified() throws Exception {
        HttpTester response = RequestUtil.getResponse(tester, "/css/application.css", System.currentTimeMillis()+60000);

        assertThat(response.getContent(), nullValue());
        assertThat(response.getStatus(), is(HttpServletResponse.SC_NOT_MODIFIED));
    }

    @Test
    public void testRespondsWithContentWhenThebundleHasBeenModified() throws Exception {
        HttpTester response = RequestUtil.getResponse(tester, "/css/application.css", 100L);

        assertThat(response.getContent(),
                equalTo("html, body { margin: 0; }\r\nbody { background-color: #000; }\r\n"));
    }

    @Test
    public void testServesPrebuiltBundles() throws Exception {
        HttpTester response = RequestUtil.getResponse(tester, "/css/prebuilt.css");
        assertThat(response.getContent(),
                equalTo("body { color: pink; }"));
    }
}