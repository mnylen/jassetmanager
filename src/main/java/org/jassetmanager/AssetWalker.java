package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletContext;
import java.util.Set;

public class AssetWalker {
    public static void walkAssetTree(@NotNull ServletContext context,
                                     @NotNull String rootPath,
                                     @NotNull AssetVisitor visitor) {
        @SuppressWarnings("unchecked")
        Set<String> assetPaths = context.getResourcePaths(rootPath);
        if (assetPaths == null) {
            return;
        }
        
        for (String assetPath : assetPaths) {
            if (isDirectory(assetPath)) {
                walkAssetTree(context, assetPath, visitor);
            } else {
                visitor.visitAsset(assetPath);
            }
        }
    }

    private static boolean isDirectory(String path) {
        return path.charAt(path.length()-1) == '/';
    }
}
