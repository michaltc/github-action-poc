package com.citrix.microapps.bundlegen.bundles;

import java.net.URI;

import com.citrix.microapps.bundlegen.ValidationException;
import com.citrix.microapps.bundlegen.pojo.MetadataIn;
import com.citrix.microapps.bundlegen.pojo.MetadataOut;

/**
 * Bundle with all information from filesystem and metadata file.
 */
public class Bundle {
    private final FsBundle fs;
    private final MetadataIn metadata;

    public Bundle(FsBundle fs, MetadataIn metadata) {
        validate(fs, metadata);
        this.fs = fs;
        this.metadata = metadata;
    }

    private static void validate(FsBundle fs, MetadataIn metadata) {
        if (!fs.getVendor().equals(metadata.getVendor())) {
            throw new ValidationException("Bundle vendor from FS structure does not match metadata: "
                    + fs + ", fs " + fs.getVendor() + ", metadata " + metadata.getVendor());
        }

        if (!fs.getId().equals(metadata.getId())) {
            throw new ValidationException("Bundle ID from FS structure does not match metadata: "
                    + fs + ", fs " + fs.getId() + ", metadata " + metadata.getId());
        }

        // TODO: Version check
    }

    public FsBundle getFs() {
        return fs;
    }

    public MetadataIn getMetadata() {
        return metadata;
    }

    public MetadataOut toBundlesMetadata(URI bundlesRepository, String md5Hex) {
        return new MetadataOut(
                metadata.getId(),
                metadata.getVendor(),
                metadata.getTitle(),
                metadata.getDescription(),
                metadata.getVersion(),
                metadata.getMaServerVersion(),
                metadata.getCategories(),
                metadata.getExportedTimestamp(),
                metadata.getMicroapps(),
                fs.getArchiveUrl(bundlesRepository),
                md5Hex);
    }

    @Override
    public String toString() {
        return fs.toString();
    }
}
