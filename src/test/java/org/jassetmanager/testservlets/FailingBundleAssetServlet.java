package org.jassetmanager.testservlets;

import org.jassetmanager.AssetBundle;
import org.jassetmanager.AssetException;
import org.jassetmanager.AssetFile;
import org.jassetmanager.Manipulator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class FailingBundleAssetServlet extends SimpleAssetConcatenationServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        super.config.addPostManipulator(new Manipulator() {
            public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content) throws AssetException {
                throw new AssetException("I, Fail");
            }
        });
    }
}
