package org.jassetmanager;

/**
 * A simple file pattern that does exact match of the given name to
 * asset.
 *
 * @author Mikko Nylén
 */
public class SimpleFilePattern implements FilePattern {
    private final String name;

    /**
     * Creates a new <code>SimpleFilePattern</code> that matches asset files against
     * the given name.
     * 
     * @param name the name to match against
     */
    public SimpleFilePattern(String name) {
        this.name = name;
    }

    public boolean matches(Asset asset) {
        return asset.getContextPath().equals(name);
    }
}
