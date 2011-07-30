package org.jassetmanager.coffeescript;

import org.jcoffeescript.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompileOptions {
    private static final String[] DEFAULT_COFFEESCRIPT_EXTENSIONS = new String[] { ".coffee" };
    private String[] extensions;
    private boolean bare;
    
    public CompileOptions() {
        this.extensions = DEFAULT_COFFEESCRIPT_EXTENSIONS;
        this.bare = false;
    }

    public CompileOptions setFileExtensions(String[] extensions) {
        this.extensions = extensions;
        return this;
    }

    public CompileOptions setBare(boolean bare) {
        this.bare = bare;
        return this;
    }

    public Collection<Option> toJCoffeeScriptOptions() {
        List<Option> options = new ArrayList<Option>();

        if (this.bare) {
            options.add(Option.BARE);
        }

        return options;
    }

    public boolean isBare() {
        return bare;
    }

    public String[] getExtensions() {
        return extensions;
    }
}
