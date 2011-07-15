package org.jassetmanager;

import org.jassetmanager.testmanipulators.CounterManipulator;
import org.jassetmanager.testservlets.CachingAssetServlet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

public class AssetServletBuildStrategiesTest {

    @Before
    public void setUp() {
        CounterManipulator.reset();
    }

    @Test
    public void testCachesWhenCacheTurnedOn() throws Exception {
        ServletTester tester = new ServletTester();
        tester.setClassLoader(CachingAssetServlet.class.getClassLoader());
        tester.setContextPath("/");
        tester.setResourceBase("src/test/resources");
        tester.addServlet(CachingAssetServlet.class, "*.css");
        tester.start();

        RequestUtil.getResponse(tester, "/css/application.css");
        HttpTester secondResponse = RequestUtil.getResponse(tester, "/css/application.css");

        tester.stop();

        assertThat(secondResponse.getContent(), equalTo(
                        "/* Counter: 1 */\r\n" +
                        "html, body { margin: 0; }\r\n" +
                        "body { background-color: #000; }\r\n"));

    }
}