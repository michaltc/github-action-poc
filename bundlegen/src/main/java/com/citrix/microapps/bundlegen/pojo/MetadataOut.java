package com.citrix.microapps.bundlegen.pojo;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Metadata of one bundle written to `bundles.json`.
 */
public class MetadataOut {
    private final MetadataIn metadataIn;
    private final URI downloadUrl;
    private final String md5Checksum;

    public MetadataOut(MetadataIn metadataIn,
                       URI downloadUrl,
                       String md5Checksum) {
        this.metadataIn = metadataIn;
        this.downloadUrl = downloadUrl;
        this.md5Checksum = md5Checksum;
    }

    @JsonUnwrapped
    public MetadataIn getMetadataIn() {
        return metadataIn;
    }

    public URI getDownloadUrl() {
        return downloadUrl;
    }

    public String getMd5Checksum() {
        return md5Checksum;
    }
}
