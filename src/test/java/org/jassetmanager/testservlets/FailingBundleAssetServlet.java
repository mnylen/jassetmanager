package org.jassetmanager.testservlets;

import org.jassetmanager.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;

public class FailingBundleAssetServlet extends SimpleAssetConcatenationServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        super.config.addPostManipulator(new Manipulator() {
            public void preManipulate(Asset asset, boolean isLast) throws AssetException, IOException {
                
            }

            public void postManipulate(Bundle bundle) throws AssetException, IOException {
                throw new AssetException("I, Fail");
            }
        });
    }
}
