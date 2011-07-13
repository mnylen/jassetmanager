package org.jassetmanager;

import javax.servlet.ServletContext;
import java.util.Set;

public class AssetWalker {
    public static void walkAssetTree(ServletContext context, String rootPath, AssetVisitor visitor) {
        @SuppressWarnings("unchecked")
        Set<String> assetPaths = context.getResourcePaths(rootPath);

        for (String assetPath : assetPaths) {
            System.out.println(assetPath);
            
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
