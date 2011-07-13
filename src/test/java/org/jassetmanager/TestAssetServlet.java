package org.jassetmanager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class TestAssetServlet extends AssetServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.configureBundle("/css/application.css", "text/css", BrowserMatchers.ANY,
                new AssetBundleConfiguration()
                    .addFilePattern(new RegexFilePattern("/reset.css"))
                    .addFilePattern(new RegexFilePattern("/main.css")));
    }
}
