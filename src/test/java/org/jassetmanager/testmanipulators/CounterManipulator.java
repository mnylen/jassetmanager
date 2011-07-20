package org.jassetmanager.testmanipulators;

import org.jassetmanager.*;

import java.io.IOException;

public class CounterManipulator implements Manipulator {
    public static int counter = 1;

    public static void reset() {
        counter = 1;
    }

    public void preManipulate(Asset asset, boolean isLast) throws AssetException, IOException {

    }

    public void postManipulate(Bundle bundle) throws AssetException, IOException {
        byte[] manipulatedContent = new StringBuilder()
                    .append("/* Counter: ").append(counter).append(" */\r\n")
                    .append(new String(bundle.getContent())).toString().getBytes();

        bundle.setManipulatedContent(manipulatedContent);
        counter++;
    }
}
