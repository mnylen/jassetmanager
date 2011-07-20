package org.jassetmanager.testmanipulators;

import org.jassetmanager.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileNameAppenderManipulator implements Manipulator {
    public void preManipulate(Asset asset, boolean isLast) throws AssetException, IOException {
        byte[] manipulatedContent = new StringBuilder("/* ")
                .append(asset.getContextPath())
                .append(" */\r\n")
                .append(new String(asset.getContent()))
                .toString()
                .getBytes();
        
        asset.setManipulatedContent(manipulatedContent);
    }

    public void postManipulate(Bundle bundle) throws AssetException, IOException {
        
    }
}
