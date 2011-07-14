package org.jassetmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddCopyrightNoticeManipulator implements Manipulator {
    public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(content.length);
            out.write("/**\r\n".getBytes());
            out.write(" * Copyright (c) 2011 Some Organization All Rights Reserved\r\n".getBytes());
            out.write(" */\r\n\r\n".getBytes());
            out.write(content);

            return out.toByteArray();
        } catch (IOException e) {
            return content;
        }
    }
}
