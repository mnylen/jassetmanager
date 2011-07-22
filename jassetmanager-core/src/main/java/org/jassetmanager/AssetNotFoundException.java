package org.jassetmanager;

import java.io.FileNotFoundException;

public class AssetNotFoundException extends FileNotFoundException {
    public AssetNotFoundException() {
    }

    public AssetNotFoundException(String s) {
        super(s);
    }
}
