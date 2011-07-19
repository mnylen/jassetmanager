package org.jassetmanager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AssetNotFoundException extends FileNotFoundException {
    public AssetNotFoundException() {
    }

    public AssetNotFoundException(String s) {
        super(s);
    }
}
