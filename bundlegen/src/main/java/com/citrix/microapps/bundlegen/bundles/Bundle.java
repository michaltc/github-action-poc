package com.citrix.microapps.bundlegen.bundles;

import com.citrix.microapps.bundlegen.ValidationException;
import com.citrix.microapps.bundlegen.pojo.Metadata;

/**
 * Bundle with all information from filesystem and metadata file.
 */
public class Bundle {
    private final FsBundle fs;
    private final Metadata metadata;

    public Bundle(FsBundle fs, Metadata metadata) {
        validate(fs, metadata);
        this.fs = fs;
        this.metadata = metadata;
    }

    private static void validate(FsBundle fs, Metadata metadata) {
        if (!fs.getVendor().equals(metadata.getVendor())) {
            throw new ValidationException("Bundle vendor from FS structure does not match metadata: "
                    + fs + ", fs " + fs.getVendor() + ", metadata " + metadata.getVendor());
        }

        if (!fs.getId().equals(metadata.getId())) {
            throw new ValidationException("Bundle ID from FS structure does not match metadata: "
                    + fs + ", fs " + fs.getId() + ", metadata " + metadata.getId());
        }
    }

    public FsBundle getFs() {
        return fs;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return fs.toString();
    }
}
