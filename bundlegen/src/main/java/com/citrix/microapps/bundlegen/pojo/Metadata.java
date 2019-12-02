package com.citrix.microapps.bundlegen.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Metadata of a bundle from `metadata.json`.
 */
public class Metadata {
    private final String id;
    private final String vendor;

    private final String title;
    private final String description;
    private final String version;
    private final String maServerVersion;
    private final List<Category> categories;
    private final Long exportedTime;
    private final List<Microapp> microapps;

    @JsonCreator
    public Metadata(@JsonProperty(value="id", required = true) String id,
                    @JsonProperty(value="vendor", required = true) String vendor,
                    @JsonProperty(value="title", required = true) String title,
                    @JsonProperty(value="description", required = true) String description,
                    @JsonProperty(value="version", required = true) String version,
                    @JsonProperty(value="maServerVersion", required = true) String maServerVersion,
                    @JsonProperty(value="categories", required = true) List<Category> categories,
                    @JsonProperty(value="exportedTime", required = true) Long exportedTime,
                    @JsonProperty(value="microapps", required = true) List<Microapp> microapps) {
        this.id = id;
        this.vendor = vendor;
        this.title = title;
        this.description = description;
        this.version = version;
        this.maServerVersion = maServerVersion;
        this.categories = categories;
        this.exportedTime = exportedTime;
        this.microapps = microapps;
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

    public Long getExportedTime() {
        return exportedTime;
    }

    public List<Microapp> getMicroapps() {
        return microapps;
    }
}
