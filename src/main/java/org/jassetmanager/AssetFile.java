package org.jassetmanager;

public class AssetFile {
    private final String path;

    public AssetFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetFile assetFile = (AssetFile) o;

        if (path != null ? !path.equals(assetFile.path) : assetFile.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
