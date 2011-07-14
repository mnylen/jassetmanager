package org.jassetmanager.testservlets;

import org.jassetmanager.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class NonCachingAssetServlet extends AssetServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        super.setCache(false);

        AssetBundleConfiguration bundleConfig = new AssetBundleConfiguration()
                .addFilePattern(new RegexFilePattern("/reset.css"))
                .addFilePattern(new RegexFilePattern("/main.css"))
                .addPostManipulator(new CounterManipulator());

        super.configureBundle("/css/application.css", "text/css", BrowserMatchers.ANY, bundleConfig);
    }
}
