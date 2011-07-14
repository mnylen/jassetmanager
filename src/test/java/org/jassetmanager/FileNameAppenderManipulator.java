package org.jassetmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileNameAppenderManipulator implements Manipulator {
    public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content) {
        if (assetFile != null) {
            ByteArrayOutputStream manipulatedContent =
                    new ByteArrayOutputStream(content.length + assetFile.getPath().length() + "/*  */\r\n".length());

            try {
                manipulatedContent.write( ("/* " + assetFile.getPath() + " */\r\n").getBytes());
                manipulatedContent.write(content);

                return manipulatedContent.toByteArray();
            } catch (IOException e) {
                
            }
        }

        return content;
    }
}
