package com.citrix.microapps.bundlegen.pojo;

import java.net.URI;
import java.util.List;

/**
 * Metadata of one bundle written to `bundles.json`.
 */
public class MetadataOut {
    private final String id;
    private final String vendor;

    private final String title;
    private final String description;
    private final String version;
    private final String maServerVersion;
    private final List<Category> categories;
    private final long exportedTimestamp;
    private final List<Microapp> microapps;

    private final URI downloadUrl;
    private final String md5Hex;

    public MetadataOut(String id,
                       String vendor,
                       String title,
                       String description,
                       String version,
                       String maServerVersion,
                       List<Category> categories,
                       long exportedTimestamp,
                       List<Microapp> microapps,
                       URI downloadUrl, String md5Hex) {
        this.id = id;
        this.vendor = vendor;
        this.title = title;
        this.description = description;
        this.version = version;
        this.maServerVersion = maServerVersion;
        this.categories = categories;
        this.exportedTimestamp = exportedTimestamp;
        this.microapps = microapps;
        this.downloadUrl = downloadUrl;
        this.md5Hex = md5Hex;
    }

    public String getId() {
        return id;
    }

    public String getVendor() {
        return vendor;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getMaServerVersion() {
        return maServerVersion;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public long getExportedTimestamp() {
        return exportedTimestamp;
    }

    public List<Microapp> getMicroapps() {
        return microapps;
    }

    public URI getDownloadUrl() {
        return downloadUrl;
    }

    public String getMd5Hex() {
        return md5Hex;
    }
}
