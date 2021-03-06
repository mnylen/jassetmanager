package org.jassetmanager.testmanipulators;

import org.jassetmanager.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddCopyrightNoticeManipulator implements Manipulator {
    public void preManipulate(Asset asset, boolean isLast) throws AssetException, IOException {
        
    }

    public void postManipulate(Bundle bundle) throws AssetException, IOException {
        byte[] manipulatedContent = new StringBuilder()
                .append("/**\r\n")
                .append(" * Copyright (c) 2011 Some Organization All Rights Reserved\r\n")
                .append(" */\r\n\r\n")
                .append(new String(bundle.getContent()))
                .toString()
                .getBytes();

        bundle.setManipulatedContent(manipulatedContent);
    }
}
