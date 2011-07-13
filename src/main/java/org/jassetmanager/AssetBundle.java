package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetBundle {
    private byte[] content;
    private boolean built;
    private final ServletContext context;
    private final AssetBundleConfiguration config;
    private static final byte[] ASSET_SEPARATOR = new byte[] { '\r', '\n' };

    public AssetBundle(@NotNull AssetBundleConfiguration config,
                       @NotNull ServletContext context) {

        this.context = context;
        this.config = config;
        this.content = new byte[0];
        this.built = false;
    }

    public boolean isBuilt() {
        return this.built;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void build(@NotNull List<String> allFilePaths) throws IOException {

        this.content = new byte[0];
        this.built = false;
        
        Map<Integer, List<String>> contentMap = new HashMap<Integer, List<String>>();
        int maxPosition = 0;

        for (String filePath : allFilePaths) {
            int position = this.config.getContentPosition(filePath);
            if (position == -1) {
                continue;
            } else if (position > maxPosition) {
                maxPosition = position;
            }

            if (!(contentMap.containsKey(position))) {
                contentMap.put(position, new ArrayList<String>());
            }

            contentMap.get(position).add(filePath);
        }

        for (int position = 0; position <= maxPosition; position++) {
            if (!(contentMap.containsKey(position))) {
                continue;
            }
            
            List<String> filePaths = contentMap.get(position);
            for (String filePath : filePaths) {
                readAndAppendContent(this.context, filePath);
            }
        }

        this.built = true;
    }

    private void readAndAppendContent(ServletContext context, String filePath) throws IOException {
        byte[] buffer = new byte[1024];
        InputStream is = null;

        try {
            is = context.getResourceAsStream(filePath);
            if (is == null) {
                throw new IOException("Could not open stream to asset '" + filePath + "'");
            }

            while (is.available() > 0) {
                int length = is.read(buffer);

                if (length != -1) {
                    appendContent(buffer, length);
                }
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
    }

    private void appendContent(byte[] append, int appendLength) {
        int newContentLength = this.content.length + ASSET_SEPARATOR.length + appendLength;

        byte[] newContent = new byte[newContentLength];
        System.arraycopy(this.content, 0, newContent, 0, this.content.length);
        System.arraycopy(append, 0, newContent, this.content.length, appendLength);
        System.arraycopy(ASSET_SEPARATOR, 0, newContent, (this.content.length + appendLength), ASSET_SEPARATOR.length);

        this.content = newContent;
    }
}
