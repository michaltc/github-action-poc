package com.citrix.microapps.bundlegen.bundles;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.citrix.microapps.bundlegen.pojo.MetadataIn;
import com.citrix.microapps.bundlegen.pojo.MetadataOut;

/**
 * Bundle with all information from filesystem and metadata file.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Bundle {
    private final FsBundle fs;
    private final Optional<MetadataIn> metadata;
    private final List<ValidationException> issues;

    public Bundle(FsBundle fs, Optional<MetadataIn> metadata, List<ValidationException> issues) {
        this.fs = fs;
        this.metadata = metadata;
        this.issues = issues;
    }

    public FsBundle getFs() {
        return fs;
    }

    public MetadataIn getMetadata() {
        return metadata.orElseThrow(() -> new IllegalStateException("No metadata"));
    }

    public List<ValidationException> getIssues() {
        return issues;
    }

    public MetadataOut toBundlesMetadata(URI bundlesRepository, String md5Hex) {
        MetadataIn m = getMetadata();
        return new MetadataOut(
                m.getId(),
                m.getVendor(),
                m.getTitle(),
                m.getDescription(),
                m.getVersion(),
                m.getMaServerVersion(),
                m.getCategories(),
                m.getExportedTimestamp(),
                m.getMicroapps(),
                fs.getArchiveUrl(bundlesRepository),
                md5Hex);
    }

    @Override
    public String toString() {
        return fs.toString();
    }
}
