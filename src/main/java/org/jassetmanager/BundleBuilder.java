package org.jassetmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class BundleBuilder {
    private static final byte[] ASSET_SEPARATOR = new byte[] { '\r', '\n' };
    private final FileSystem fs;
    private final BundleConfiguration configuration;

    public BundleBuilder(BundleConfiguration configuration,
                         FileSystem fs) {

        this.configuration = configuration;
        this.fs = fs;
    }

    public void build(Bundle bundle) throws IOException, AssetException {
        BundleAssets assets = new FileSystemBundleAssets(
                this.fs,
                this.configuration.getContextRootPath(),
                this.configuration.getFilePatterns());

        if (this.configuration.getBuildStrategy().isRebuildNeeded(bundle, assets)) {
            doBuild(bundle, assets);
        }
    }

    private void doBuild(Bundle bundle, BundleAssets assets) throws IOException, AssetException {
        ByteArrayOutputStream contentStream = new ByteArrayOutputStream();
        Iterator<Asset> it = assets.getAssets();
        while (it.hasNext()) {
            Asset asset = it.next();
            for (Manipulator manipulator : this.configuration.getPreManipulators()) {
                manipulator.preManipulate(asset, it.hasNext());
            }

            contentStream.write(asset.getContent());
            contentStream.write(ASSET_SEPARATOR);
        }
        
        bundle.update(assets, contentStream.toByteArray());

        for (Manipulator manipulator : this.configuration.getPostManipulators()) {
            manipulator.postManipulate(bundle);
        }
    }
}
