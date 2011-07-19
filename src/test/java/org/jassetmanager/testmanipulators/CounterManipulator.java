package org.jassetmanager.testmanipulators;

import org.jassetmanager.*;

import java.io.IOException;

public class CounterManipulator implements Manipulator {
    public static int counter = 1;

    public static void reset() {
        counter = 1;
    }

    public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content) {
        return new StringBuilder("/* Counter: " + counter++ + " */\r\n")
                    .append(new String(content)).toString().getBytes();
    }

    public void preManipulate(Asset asset, boolean isLast) throws AssetException, IOException {

    }

    public void postManipulate(Bundle bundle) throws AssetException, IOException {
        
    }
}
