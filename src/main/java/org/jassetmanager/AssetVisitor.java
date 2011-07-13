package org.jassetmanager;

import com.sun.istack.internal.NotNull;

public interface AssetVisitor {
    public void visitAsset(@NotNull String path);
}
