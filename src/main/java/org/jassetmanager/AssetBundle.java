package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AssetBundle {
    private byte[] content;
    private boolean built;
    private final ServletContext context;
    private final AssetBundleConfiguration config;
    private static final byte[] ASSET_SEPARATOR = new byte[] { '\r', '\n' };
    private long builtAt;
    
    public AssetBundle(@NotNull AssetBundleConfiguration config,
                       @NotNull ServletContext context) {

        this.context = context;
        this.config = config;
        this.content = new byte[0];
        this.built = false;
    }

    public AssetBundleConfiguration getConfiguration() {
        return config;
    }

    public boolean isBuilt() {
        return this.built;
    }

    public byte[] getContent() {
        return this.content;
    }

    public long getBuiltAt() {
        return builtAt;
    }

    public void build(@NotNull List<AssetFile> allAssetFiles) throws IOException {
        this.content = new byte[0];
        this.built = false;

        Map<Integer, List<AssetFile>> contentMap = createContentMap(allAssetFiles);
        readAndAppendFilesFromContentMap(contentMap);

        this.builtAt = System.currentTimeMillis();
        this.built = true;
    }

    public long getLastModified(List<AssetFile> allFiles) throws IOException {
        long max = 0;

        for (AssetFile file : allFiles) {
            if (this.config.getContentPosition(file) != -1) {
                long lastModified = file.getLastModified();

                if (lastModified > max) {
                    max = lastModified;
                }
            }
        }

        return max;
    }

    private void readAndAppendFilesFromContentMap(Map<Integer, List<AssetFile>> contentMap) throws IOException {
        List<Integer> keys = new ArrayList<Integer>(contentMap.keySet());
        Collections.sort(keys);

        ByteArrayOutputStream to = new ByteArrayOutputStream();

        for (Integer position : keys) {
            List<AssetFile> files = contentMap.get(position);
            readAndAppendFiles(files, to);
        }

        this.content = postManipulate(to.toByteArray());
    }

    private void readAndAppendFiles(List<AssetFile> assetFiles, ByteArrayOutputStream to) throws IOException {
        for (AssetFile assetFile : assetFiles) {
            readAndAppendFile(assetFile, to);
        }
    }

    private Map<Integer, List<AssetFile>> createContentMap(List<AssetFile> allAssetFiles) {
        Map<Integer, List<AssetFile>> contentMap = new HashMap<Integer, List<AssetFile>>();

        for (AssetFile assetFile : allAssetFiles) {
            int position = this.config.getContentPosition(assetFile);
            if (position == -1) {
                continue;
            }

            if (!(contentMap.containsKey(position))) {
                contentMap.put(position, new ArrayList<AssetFile>());
            }

            contentMap.get(position).add(assetFile);
        }

        return contentMap;
    }

    private void readAndAppendFile(AssetFile assetFile, ByteArrayOutputStream to) throws IOException {
        InputStream is = null;

        try {
            is = this.context.getResourceAsStream(assetFile.getPath());
            if (is == null) {
                throw new IOException("Could not open stream to asset '" + assetFile + "'");
            }

            to.write(preManipulate(assetFile, ResourceUtil.readInputStream(is)));
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

    private byte[] preManipulate(AssetFile assetFile, byte[] content) {
        for (Manipulator manipulator : this.config.getPreManipulators()) {
            content = manipulator.manipulate(this, assetFile, content);
        }

        return content;
    }

    private byte[] postManipulate(byte[] content) {
        for (Manipulator manipulator : this.config.getPostManipulators()) {
            content = manipulator.manipulate(this, null, content);
        }

        return content;
    }
}
