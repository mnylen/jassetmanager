package org.jassetmanager.testservlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class FailingBundleDebugModeAssetServlet extends FailingBundleAssetServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        super.setDebug(true);
    }
}
