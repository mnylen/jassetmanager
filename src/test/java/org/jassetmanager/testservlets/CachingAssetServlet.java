package org.jassetmanager.testservlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class CachingAssetServlet extends NonCachingAssetServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        super.setCache(true);
    }
}
