package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.*;

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

        Map<Integer, List<String>> contentMap = createContentMap(allFilePaths);
        readAndAppendFilesFromContentMap(contentMap);
        
        this.built = true;
    }

    private void readAndAppendFilesFromContentMap(Map<Integer, List<String>> contentMap) throws IOException {
        List<Integer> keys = new ArrayList<Integer>(contentMap.keySet());
        Collections.sort(keys);

        ByteArrayOutputStream to = new ByteArrayOutputStream();

        for (Integer position : keys) {
            List<String> files = contentMap.get(position);
            readAndAppendFiles(files, to);
        }

        this.content = to.toByteArray();
    }

    private void readAndAppendFiles(List<String> files, ByteArrayOutputStream to) throws IOException {
        for (String file : files) {
            readAndAppendFile(file, to);
        }
    }

    private Map<Integer, List<String>> createContentMap(List<String> allFilePaths) {
        Map<Integer, List<String>> contentMap = new HashMap<Integer, List<String>>();

        for (String filePath : allFilePaths) {
            int position = this.config.getContentPosition(filePath);
            if (position == -1) {
                continue;
            }

            if (!(contentMap.containsKey(position))) {
                contentMap.put(position, new ArrayList<String>());
            }

            contentMap.get(position).add(filePath);
        }

        return contentMap;
    }

    private void readAndAppendFile(String filePath, ByteArrayOutputStream to) throws IOException {
        byte[] buffer = new byte[1024];
        InputStream is = null;

        try {
            is = this.context.getResourceAsStream(filePath);
            if (is == null) {
                throw new IOException("Could not open stream to asset '" + filePath + "'");
            }

            to.write(ResourceUtil.readInputStream(is));
            to.write(ASSET_SEPARATOR);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
    }
}
