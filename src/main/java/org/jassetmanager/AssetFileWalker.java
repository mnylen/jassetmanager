package org.jassetmanager;

import com.sun.istack.internal.NotNull;

import javax.servlet.ServletContext;
import java.util.Set;

public class AssetFileWalker {
    public static void walkAssetFiles(@NotNull ServletContext context,
                                      @NotNull String rootPath,
                                      @NotNull AssetFileVisitor visitor) {
        @SuppressWarnings("unchecked")
        Set<String> assetPaths = context.getResourcePaths(rootPath);
        if (assetPaths == null) {
            return;
        }
        
        for (String assetPath : assetPaths) {
            if (isDirectory(assetPath)) {
                walkAssetFiles(context, assetPath, visitor);
            } else {
                visitor.visitFile(new AssetFile(assetPath));
            }
        }
    }

    private static boolean isDirectory(String path) {
        return path.charAt(path.length()-1) == '/';
    }
}
