package org.jassetmanager;

import java.util.ArrayList;
import java.util.List;

public class BundleConfiguration {
    private final static BuildStrategy DEFAULT_BUILD_STRATEGY = BuildStrategies.REBUILD_IF_MODIFIED;
    private final List<FilePattern> filePatterns;
    private final List<Manipulator> preManipulators;
    private final List<Manipulator> postManipulators;
    private String contextRootPath;
    private BuildStrategy buildStrategy;

    public BundleConfiguration() {
        this.filePatterns = new ArrayList<FilePattern>();
        this.preManipulators = new ArrayList<Manipulator>();
        this.postManipulators = new ArrayList<Manipulator>();
        this.contextRootPath = AssetServlet.ASSET_ROOT_PATH;
        this.buildStrategy = BuildStrategies.REBUILD_IF_MODIFIED;
    }

    public BundleConfiguration(BundleConfiguration baseConfiguration) {
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

    public BundleConfiguration setContextRootPath(String contextRootPath) {
        this.contextRootPath = contextRootPath;
        return this;
    }

    public BundleConfiguration includeConfiguration(BundleConfiguration configuration) {
        for (FilePattern pattern : configuration.getFilePatterns()) {
            this.addFilePattern(pattern);
        }

        for (Manipulator preManipulator : configuration.getPreManipulators()) {
            this.addPreManipulator(preManipulator);
        }

        for (Manipulator postManipulator : configuration.getPostManipulators()) {
            this.addPostManipulator(postManipulator);
        }

        if (this.buildStrategy == DEFAULT_BUILD_STRATEGY) {
            this.buildStrategy = configuration.getBuildStrategy();
        }

        if (this.contextRootPath.equals(AssetServlet.ASSET_ROOT_PATH)) {
            this.contextRootPath = configuration.getContextRootPath();
        }
        
        return this;
    }

    public List<FilePattern> getFilePatterns() {
        return filePatterns;
    }

    public BundleConfiguration addFilePattern(FilePattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern must not be null");
        }

        this.filePatterns.add(pattern);
        return this;
    }

    public BundleConfiguration addFilePattern(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern must not be null");
        }

        this.filePatterns.add(new SimpleFilePattern(pattern));
        return this;
    }

    public List<Manipulator> getPreManipulators() {
        return preManipulators;
    }

    public BundleConfiguration addPreManipulator(Manipulator manipulator) {
        this.preManipulators.add(manipulator);
        return this;
    }

    public List<Manipulator> getPostManipulators() {
        return postManipulators;
    }

    public BundleConfiguration addPostManipulator(Manipulator manipulator) {
        this.postManipulators.add(manipulator);
        return this;
    }
}
