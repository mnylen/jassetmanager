package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetBundle {
    private final List<FilePattern> filePatterns;
    private byte[] content;

    public AssetBundle() {
        this.filePatterns = new ArrayList<FilePattern>();
        this.content = new byte[0];
    }

    public AssetBundle addFilePattern(FilePattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern must not be null");
        }
        
        this.filePatterns.add(pattern);
        return this;
    }

    public AssetBundle addFilePattern(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern must not be null");
        }

        this.filePatterns.add(new RegexFilePattern(pattern));
        return this;
    }

    public boolean includesFile(@NotNull String fileName) {
        for (FilePattern pattern : this.filePatterns) {
            if (pattern.matches(fileName)) {
                return true;
            }
        }

        return false;
    }

    public AssetBundle addContent(byte[] content) {
        byte[] newContent = new byte[this.content.length+content.length+2];
        System.arraycopy(this.content, 0, newContent, 0, this.content.length);
        System.arraycopy(content, 0, newContent, this.content.length, content.length);
        newContent[newContent.length-2] = '\r';
        newContent[newContent.length-1] = '\n';

        this.content = newContent;
        return this;
    }

    public byte[] getContent() {
        return content;
    }
}
