package org.jassetmanager;

public abstract class BuildStrategies {
    public static final BuildStrategy ALWAYS_REBUILD = new AlwaysRebuildStrategy();
    public static final BuildStrategy REBUILD_IF_MODIFIED = new RebuildIfModifiedStrategy();
    public static final BuildStrategy BUILD_ONCE = new BuildOnceStrategy();
}
