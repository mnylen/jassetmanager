package org.jassetmanager.coffeescript;

import org.jassetmanager.Asset;
import org.jassetmanager.AssetException;
import org.jassetmanager.Bundle;
import org.jassetmanager.Manipulator;
import org.jcoffeescript.JCoffeeScriptCompileException;
import org.jcoffeescript.JCoffeeScriptCompiler;

import java.io.IOException;

public class CoffeeScriptCompileManipulator implements Manipulator {
    private final CompileOptions options;
    private final JCoffeeScriptCompiler compiler;

    public CoffeeScriptCompileManipulator(CompileOptions options) {
        this.options = options;
        this.compiler = new JCoffeeScriptCompiler(this.options.toJCoffeeScriptOptions());
    }

    public CoffeeScriptCompileManipulator() {
        this(new CompileOptions());
    }

    public void preManipulate(Asset asset, boolean isLast) throws AssetException, IOException {
        if (!(isCoffeeScriptAsset(asset))) {
            return;
        }
        
        try {
            String compiled = this.compiler.compile(new String(asset.getContent()));
            asset.setManipulatedContent(compiled.getBytes());
        } catch (JCoffeeScriptCompileException e) {
            throw new AssetException(e);
        }
    }

    private boolean isCoffeeScriptAsset(Asset asset) {
        int lastDotPos = asset.getContextPath().lastIndexOf('.');

        if (lastDotPos != -1) {
            String ext = asset.getContextPath().substring(lastDotPos);

            for (String compiledExt : this.options.getExtensions()) {
                if (compiledExt.equals(ext)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void postManipulate(Bundle bundle) throws AssetException, IOException {
        throw new UnsupportedOperationException(
                "CoffeeScriptCompileManipulator should be used as pre manipulator.");
    }
}
