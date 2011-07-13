package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AssetBundleConfiguration {
    private final List<FilePattern> filePatterns;

    public AssetBundleConfiguration() {
        this.filePatterns = new ArrayList<FilePattern>();
    }

    public AssetBundleConfiguration(AssetBundleConfiguration baseConfiguration) {
        this();
        includeConfiguration(baseConfiguration);
    }

    public AssetBundleConfiguration includeConfiguration(AssetBundleConfiguration configuration) {
        for (FilePattern pattern : configuration.getFilePatterns()) {
            this.addFilePattern(pattern);
        }
        
        return this;
    }

    public List<FilePattern> getFilePatterns() {
        return filePatterns;
    }

    public AssetBundleConfiguration addFilePattern(FilePattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern must not be null");
        }

        this.filePatterns.add(pattern);
        return this;
    }

    public AssetBundleConfiguration addFilePattern(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern must not be null");
        }

        this.filePatterns.add(new RegexFilePattern(pattern));
        return this;
    }

    public int getContentPosition(@NotNull String fileName) {
        int i = 0;

        for (FilePattern pattern : this.filePatterns) {
            if (pattern.matches(fileName)) {
                return i;
            }

            i++;
        }

        return -1;
    }
}
