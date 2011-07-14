package org.jassetmanager.testmanipulators;

import org.jassetmanager.AssetBundle;
import org.jassetmanager.AssetFile;
import org.jassetmanager.Manipulator;

public class CounterManipulator implements Manipulator {
    public static int counter = 1;

    public static void reset() {
        counter = 1;
    }

    public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content) {
        return new StringBuilder("/* Counter: " + counter++ + " */\r\n")
                    .append(new String(content)).toString().getBytes();
    }
}
