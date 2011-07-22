package org.jassetmanager.testservlets;

import org.jassetmanager.BuildStrategies;
import org.jassetmanager.testmanipulators.CounterManipulator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class CachingAssetServlet extends SimpleAssetConcatenationServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        super.config.addPostManipulator(new CounterManipulator());
        super.config.setBuildStrategy(BuildStrategies.BUILD_ONCE);
    }
}
