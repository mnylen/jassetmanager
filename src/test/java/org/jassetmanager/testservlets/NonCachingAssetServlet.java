package org.jassetmanager.testservlets;

import org.jassetmanager.BuildStrategies;
import org.jassetmanager.BuildStrategy;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class NonCachingAssetServlet extends CachingAssetServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        super.config.setBuildStrategy(BuildStrategies.ALWAYS_REBUILD);
    }
}
