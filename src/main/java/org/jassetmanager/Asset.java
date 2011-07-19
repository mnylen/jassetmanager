package org.jassetmanager;

import java.io.IOException;

public class Asset {
    private final String contextPath;
    private final FileSystem fs;

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
        return fs.getContent(this);
    }
}
