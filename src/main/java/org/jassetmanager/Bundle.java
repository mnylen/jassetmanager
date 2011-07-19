package org.jassetmanager;

public class Bundle {
    private long updatedAt;
    private BundleAssets assets;
    private byte[] content;
    private boolean manipulated;
    
    public Bundle() {
        this.updatedAt = 0L;
        this.content = new byte[0];
        this.assets = new EmptyAssets();
        this.manipulated = false;
    }

    protected synchronized void update(BundleAssets assets, byte[] content) {
        this.updatedAt = System.currentTimeMillis();
        this.assets = assets;
        this.content = content;
    }

    public synchronized void setManipulatedContent(byte[] content) {
        this.content = content;
        this.manipulated = true;
    }

    public boolean isManipulated() {
        return this.manipulated;
    }

    public boolean isBuilt() {
        return this.updatedAt > 0;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public BundleAssets getAssets() {
        return assets;
    }

    public byte[] getContent() {
        return content;
    }
}
