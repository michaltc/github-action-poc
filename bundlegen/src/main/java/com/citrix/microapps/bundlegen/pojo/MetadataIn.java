package com.citrix.microapps.bundlegen.pojo;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata of a bundle loaded from `metadata.json`.
 */
public class MetadataIn {
    private final Type type;
    private final String vendor;
    private final String id;
    private final String version;
    private final String title;
    private final String description;
    private final URI iconUrl;
    private final String masVersion;
    private final List<Category> categories;
    private final String created;
    private final boolean supportsOAuthForActions;
    private final List<String> i18nLanguages;
    private final List<App> apps;
    private final List<VaResolver> vaResolvers;
    private final List<Metadata> metadata;

    @JsonCreator
    public MetadataIn(
            @JsonProperty(value = "type", required = true) Type type,
            @JsonProperty(value = "vendor", required = true) String vendor,
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "version", required = true) String version,
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "iconUrl", required = true) URI iconUrl,
            @JsonProperty(value = "masVersion", required = true) String masVersion,
            @JsonProperty(value = "categories", required = true) List<Category> categories,
            @JsonProperty(value = "created", required = true) String created,
            @JsonProperty(value = "supportsOAuthForActions", required = true) boolean supportsOAuthForActions,
            @JsonProperty(value = "i18nLanguages", required = true) List<String> i18nLanguages,
            @JsonProperty(value = "apps", required = true) List<App> apps,
            @JsonProperty(value = "vaResolvers", required = true) List<VaResolver> vaResolvers,
            @JsonProperty(value = "metadata", required = true) List<Metadata> metadata
    ) {
        this.type = type;
        this.id = id;
        this.vendor = vendor;
        this.title = title;
        this.description = description;
        this.version = version;
        this.iconUrl = iconUrl;
        this.masVersion = masVersion;
        this.categories = categories;
        this.created = created;
        this.supportsOAuthForActions = supportsOAuthForActions;
        this.i18nLanguages = i18nLanguages;
        this.apps = apps;
        this.vaResolvers = vaResolvers;
        this.metadata = metadata;

        // TODO: Validate format
        // "created": "2019-18-16T00:00:00",
    }

    public Type getType() {
        return type;
    }

    public String getVendor() {
        return vendor;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public URI getIconUrl() {
        return iconUrl;
    }

    public String getMasVersion() {
        return masVersion;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getCreated() {
        return created;
    }

    public boolean isSupportsOAuthForActions() {
        return supportsOAuthForActions;
    }

    public List<String> getI18nLanguages() {
        return i18nLanguages;
    }

    public List<App> getApps() {
        return apps;
    }

    public List<VaResolver> getVaResolvers() {
        return vaResolvers;
    }

    public List<Metadata> getMetadata() {
        return metadata;
    }
}
