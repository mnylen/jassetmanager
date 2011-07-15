package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AssetBundleConfiguration {
    private final List<FilePattern> filePatterns;
    private final List<Manipulator> preManipulators;
    private final List<Manipulator> postManipulators;
    private String contextRootPath;
    private BuildStrategy buildStrategy;

    public AssetBundleConfiguration() {
        this.filePatterns = new ArrayList<FilePattern>();
        this.preManipulators = new ArrayList<Manipulator>();
        this.postManipulators = new ArrayList<Manipulator>();
        this.contextRootPath = AssetServlet.ASSET_ROOT_PATH;
        this.buildStrategy = BuildStrategies.BUILD_ONCE;
    }

    public AssetBundleConfiguration(AssetBundleConfiguration baseConfiguration) {
        this();
        includeConfiguration(baseConfiguration);
    }

    public String getContextRootPath() {
        return contextRootPath;
    }

    public BuildStrategy getBuildStrategy() {
        return buildStrategy;
    }

    public void setBuildStrategy(BuildStrategy buildStrategy) {
        this.buildStrategy = buildStrategy;
    }

    public AssetBundleConfiguration setContextRootPath(String contextRootPath) {
        this.contextRootPath = contextRootPath;
        return this;
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

        if (this.buildStrategy == BuildStrategies.BUILD_ONCE) {
            this.buildStrategy = configuration.getBuildStrategy();
        }

        if (this.contextRootPath == AssetServlet.ASSET_ROOT_PATH) {
            this.contextRootPath = configuration.getContextRootPath();
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

    public int getContentPosition(@NotNull AssetFile assetFile) {
        int i = 0;

        for (FilePattern pattern : this.filePatterns) {
            if (pattern.matches(assetFile)) {
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
