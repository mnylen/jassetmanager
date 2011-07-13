package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AssetBundleConfiguration {
    private final List<FilePattern> filePatterns;
    private final List<Manipulator> preManipulators;
    private final List<Manipulator> postManipulators;

    public AssetBundleConfiguration() {
        this.filePatterns = new ArrayList<FilePattern>();
        this.preManipulators = new ArrayList<Manipulator>();
        this.postManipulators = new ArrayList<Manipulator>();
    }

    public AssetBundleConfiguration(AssetBundleConfiguration baseConfiguration) {
        this();
        includeConfiguration(baseConfiguration);
    }

    public AssetBundleConfiguration includeConfiguration(AssetBundleConfiguration configuration) {
        for (FilePattern pattern : configuration.getFilePatterns()) {
            this.addFilePattern(pattern);
        }

        for (Manipulator preManipulator : configuration.getPreManipulators()) {
            this.addPreManipulator(preManipulator);
        }

        for (Manipulator postManipulator : configuration.getPostManipulators()) {
            this.addPostManipulator(postManipulator);
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

    public List<Manipulator> getPreManipulators() {
        return preManipulators;
    }

    public AssetBundleConfiguration addPreManipulator(Manipulator manipulator) {
        this.preManipulators.add(manipulator);
        return this;
    }

    public List<Manipulator> getPostManipulators() {
        return postManipulators;
    }

    public AssetBundleConfiguration addPostManipulator(Manipulator manipulator) {
        this.postManipulators.add(manipulator);
        return this;
    }
}
