package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class Assets {
    private final ServletContext context;
    private final String contextRoot;
    private List<AssetFile> assetFiles;

    public Assets(ServletContext context, String contextRoot) {
        this.context = context;
        this.contextRoot = contextRoot;
    }

    public List<AssetFile> listAssets() {
        if (this.assetFiles != null) {
            return this.assetFiles;
        } else {
            final List<AssetFile> assetFiles = new ArrayList<AssetFile>();

            AssetFileWalker.walkAssetFiles(this.context, this.contextRoot, new AssetFileVisitor() {
                public void visitFile(@NotNull AssetFile assetFile) {
                    assetFiles.add(assetFile);
                }
            });

            this.assetFiles = assetFiles;
            return assetFiles;
        }
    }
}
