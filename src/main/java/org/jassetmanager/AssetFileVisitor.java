package org.jassetmanager;

import com.sun.istack.internal.NotNull;

public interface AssetFileVisitor {
    public void visitFile(@NotNull AssetFile assetFile);
}
