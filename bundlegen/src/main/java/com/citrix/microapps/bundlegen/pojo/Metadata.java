package com.citrix.microapps.bundlegen.pojo;

import java.net.URI;
import java.util.List;

/**
 * Metadata of a bundle loaded from `metadata.json`, common parts.
 */
public abstract class Metadata {
    private final Type type;  // TODO: Remove it from source json?
    private final String vendor;  // TODO: Remove it from source json?
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
    private final List<Tag> tags;  // TODO: Tech. spec. names it `metadata`, too many metadata classes, is rename ok?

    public Metadata(
            Type type,
            String vendor,
            String title,
            String description,
            URI iconUrl,
            String masVersion,
            List<Category> categories,
            String created,
            boolean supportsOAuthForActions,
            List<String> i18nLanguages,
            List<App> apps,
            List<VaResolver> vaResolvers,
            List<Tag> tags
    ) {
        this.type = type;
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
