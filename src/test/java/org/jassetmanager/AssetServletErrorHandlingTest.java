package org.jassetmanager;

import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.hamcrest.CoreMatchers;
import org.jassetmanager.testservlets.FailingBundleAssetServlet;
import org.jassetmanager.testservlets.FailingBundleDebugModeAssetServlet;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class AssetServletErrorHandlingTest {
    @Test
    public void testWhenNotInDebugModeRespondsWithInternalServerErrorAndOutputsNothing() throws Exception {
        ServletTester tester = RequestUtil.createAndStartServletTester(FailingBundleAssetServlet.class, "*.css");
        HttpTester response = RequestUtil.getResponse(tester, "/css/application.css");

        assertThat(response.getContent(), nullValue());
        assertThat(response.getStatus(), is(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void testWhenInDebugModeRespondsWithInternalServerErrorAndOutputsTheError() throws Exception {
        ServletTester tester = RequestUtil.createAndStartServletTester(FailingBundleDebugModeAssetServlet.class, "*.css");
        HttpTester response = RequestUtil.getResponse(tester, "/css/application.css");

        assertThat(response.getStatus(), is(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        assertThat(response.getContent().contains("I, Fail"), is(true));
    }
}
