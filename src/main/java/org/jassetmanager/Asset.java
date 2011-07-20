package org.jassetmanager;

import java.io.IOException;

public class Asset {
    private final String contextPath;
    private final FileSystem fs;
    private byte[] content;
    private boolean manipulated;
    
    public Asset(FileSystem fs, String contextPath) {
        this.fs = fs;
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public long getLastModified() throws IOException {
        return fs.getLastModified(this);
    }

    public byte[] getContent() throws IOException {
        if (this.content == null) {
            this.content = fs.getContent(this);
        }

        return this.content;
    }

    public void setManipulatedContent(byte[] content) {
        this.content = content;
        this.manipulated = true;
    }

    public boolean isManipulated() {
        return this.manipulated;
    }
}
