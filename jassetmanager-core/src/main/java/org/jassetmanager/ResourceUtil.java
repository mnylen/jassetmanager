package org.jassetmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

public abstract  class ResourceUtil {
    private static final int BUFFER_SIZE = 1024;

    public static byte[] readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(is.available());
        byte[] buffer = new byte[BUFFER_SIZE];
        
        while (is.available() > 0) {
            int length = is.read(buffer);
            outputStream.write(buffer, 0, length);
        }

        return outputStream.toByteArray();
    }
}
