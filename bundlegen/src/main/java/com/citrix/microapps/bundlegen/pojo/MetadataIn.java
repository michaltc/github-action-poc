package com.citrix.microapps.bundlegen.pojo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.citrix.microapps.bundlegen.bundles.FsBundle;
import com.citrix.microapps.bundlegen.bundles.ValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata of a bundle loaded from `metadata.json`.
 */
public class MetadataIn {
    // e.g. `id: "com.sapho.services.salesforce.SalesforceService"`
    private static final Pattern ID_PATTERN = Pattern.compile("[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*");

    // e.g. `created: "2019-18-16T00:00:00"`
    private static final Pattern DATE_PATTERN =
            Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}");

    // e.g. `version: "2.5.0"`
    // e.g. `masVersion: "0.8.0"`
    private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+(?:\\.[0-9]+)*");

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
    }

    public List<ValidationException> validate(FsBundle bundle) {
        ArrayList<ValidationException> issues = new ArrayList<>();

        validate(bundle, issues, ID_PATTERN, "id", id);
        validate(bundle, issues, DATE_PATTERN, "created", created);
        validate(bundle, issues, VERSION_PATTERN, "version", version);
        validate(bundle, issues, VERSION_PATTERN, "masVersion", masVersion);

        // TODO: Rules for other validations.

        return issues;
    }

    private void validate(FsBundle bundle,
                          ArrayList<ValidationException> issues,
                          Pattern pattern,
                          String field,
                          String value) {
        if (!pattern.matcher(value).matches()) {
            String message = String.format("Invalid value: field `%s`, value `%s`, pattern `%s`", field, value,
                    pattern);
            issues.add(new ValidationException(bundle, message));
        }
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
