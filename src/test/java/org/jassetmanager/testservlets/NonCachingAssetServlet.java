package org.jassetmanager.testservlets;

import org.jassetmanager.testmanipulators.CounterManipulator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class NonCachingAssetServlet extends SimpleAssetConcatenationServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        super.setCache(false);
        super.config.addPostManipulator(new CounterManipulator());
    }
}
