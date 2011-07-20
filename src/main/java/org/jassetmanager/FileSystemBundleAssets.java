package org.jassetmanager;

import java.io.IOException;
import java.util.*;

public class FileSystemBundleAssets implements BundleAssets {
    private final FileSystem fs;
    private final String root;
    private final List<FilePattern> filePatterns;
    private final Map<Integer, List<Asset>> resolvedAssets;
    
    public FileSystemBundleAssets(FileSystem fs, String root, List<FilePattern> filePatterns) {
        this.fs = fs;
        this.filePatterns = filePatterns;
        this.root = root;
        this.resolvedAssets = new HashMap<Integer, List<Asset>>();
    }

    public Iterator<Asset> getAssets() throws IOException {
        if (this.resolvedAssets.isEmpty()) {
            resolveAssets();
        }

        return new AssetIterator(this.resolvedAssets);
    }

    private void resolveAssets() throws IOException {
        for (Asset asset : this.fs.getAssets(this.root)) {
            int position = 0;
            boolean found = false;

            for (FilePattern pattern : this.filePatterns) {
                if (pattern.matches(asset)) {
                    found = true;
                    break;
                }

                position++;
            }

            if (found) {
                if (!(this.resolvedAssets.containsKey(position))) {
                    this.resolvedAssets.put(position, new ArrayList<Asset>());
                }

                this.resolvedAssets.get(position).add(asset);
            }
        }
    }

    public long getLastModified() throws IOException {
        Iterator<Asset> assets = this.getAssets();

        long lastModified = -1L;
        while (assets.hasNext()) {
            long assetLastModified = assets.next().getLastModified();

            if (assetLastModified > lastModified) {
                lastModified = assetLastModified;
            }
        }

        return lastModified;
    }
}

class AssetIterator implements Iterator<Asset> {
    private final Map<Integer, List<Asset>> map;
    private Iterator<Integer> positionIterator;
    private Iterator<Asset> positionAssetsIterator;
    
    public AssetIterator(Map<Integer, List<Asset>> map) {
        this.map = map;
        init();
    }

    public boolean hasNext() {
        return positionIterator.hasNext() || positionAssetsIterator.hasNext();
    }

    public Asset next() {
        if (!(this.positionAssetsIterator.hasNext())) {
            if (this.positionIterator.hasNext()) {
                this.positionAssetsIterator = this.map.get(this.positionIterator.next()).iterator();
            } else {
                this.positionAssetsIterator = Collections.emptyIterator();
            }
        }

        return this.positionAssetsIterator.next();
    }

    private void init() {
        List<Integer> positions = new ArrayList<Integer>(this.map.keySet());
        Collections.sort(positions);

        this.positionIterator = positions.iterator();

        if (this.positionIterator.hasNext()) {
            this.positionAssetsIterator = this.map.get(this.positionIterator.next()).iterator();
        } else {
            this.positionAssetsIterator = Collections.emptyIterator();
        }
    }

    public void remove() {
        
    }
}