package org.jassetmanager.testservlets;

import org.jassetmanager.BundleConfiguration;
import org.jassetmanager.AssetServlet;
import org.jassetmanager.BrowserMatchers;
import org.jassetmanager.RegexFilePattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class SimpleAssetConcatenationServlet extends AssetServlet {
    protected BundleConfiguration config;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.config = new BundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/reset.css"))
                .addFilePattern(new RegexFilePattern("/css/main.css"))
                .setContextRootPath("/css");

        this.configureBundle("/css/application.css", "text/css", BrowserMatchers.ANY, this.config);
    }
}
