package com.citrix.microapps.bundlegen.pojo;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata loaded from `metadata.json` for HTTP integrations.
 */
public class HttpMetadata implements Metadata {
    private final Type type;
    private final String vendor;
    private final UUID id;
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
    private final List<Tag> tags;

    @JsonCreator
    public HttpMetadata(
            @JsonProperty(value = "type", required = true) Type type,
            @JsonProperty(value = "vendor", required = true) String vendor,
            @JsonProperty(value = "id", required = true) UUID id,
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
            @JsonProperty(value = "tags", required = true) List<Tag> tags
    ) {
        this.type = type;
        this.id = id;
        this.vendor = vendor;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.masVersion = masVersion;
        this.categories = categories;
        this.created = created;
        this.supportsOAuthForActions = supportsOAuthForActions;
        this.i18nLanguages = i18nLanguages;
        this.apps = apps;
        this.vaResolvers = vaResolvers;
        this.tags = tags;
    }

    public Type getType() {
        return type;
    }

    public String getVendor() {
        return vendor;
    }

    public UUID getId() {
        return id;
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

    public List<Tag> getTags() {
        return tags;
    }
}
