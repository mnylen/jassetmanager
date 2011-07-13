package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AssetBundle {
    private final List<FilePattern> filePatterns;

    public AssetBundle() {
        this.filePatterns = new ArrayList<FilePattern>();
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
}
