package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citrix.microapps.bundlegen.pojo.DipMetadata;
import com.citrix.microapps.bundlegen.pojo.HttpMetadata;
import com.citrix.microapps.bundlegen.pojo.Metadata;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Loader and validator of bundles.
 */
public class BundlesLoader {
    private static final Logger logger = LoggerFactory.getLogger(BundlesLoader.class);

    private static final ObjectReader METADATA_READER = new ObjectMapper()
            .reader()
            .with(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .with(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
            .with(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES);

    // e.g. `id: "com.sapho.services.salesforce.SalesforceService"`
    private static final Pattern ID_PATTERN = Pattern.compile("[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*");

    // e.g. `created: "2019-18-16T00:00:00"`
    private static final Pattern DATE_PATTERN =
            Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}");

    // e.g. `version: "2.5.0"`
    // e.g. `masVersion: "0.8.0"`
    private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+(?:\\.[0-9]+)*");

    public Bundle loadBundle(FsBundle bundle) {
        logger.info("Loading bundle: {}", bundle);
        List<ValidationException> issues = new ArrayList<>();

        issues.addAll(checkMandatoryFiles(bundle, bundle.getFiles()));
        issues.addAll(checkUnknownFiles(bundle, bundle.getFiles()));

        Optional<Metadata> metadata = loadAndValidateMetadata(issues, bundle);
        return new Bundle(bundle, metadata, issues);
    }

    private Optional<Metadata> loadAndValidateMetadata(List<ValidationException> issues, FsBundle bundle) {
        Path metadataPath = bundle.getMetadataPath();

        try {
            switch (bundle.getType()) {
                case DIP:
                    DipMetadata dipMetadata = METADATA_READER
                            .forType(DipMetadata.class)
                            .readValue(metadataPath.toFile());
                    issues.addAll(validateDipMetadata(bundle, dipMetadata));
                    return Optional.of(dipMetadata);

                case HTTP:
                    HttpMetadata httpMetadata = METADATA_READER
                            .forType(HttpMetadata.class)
                            .readValue(metadataPath.toFile());
                    issues.addAll(validateHttpMetadata(bundle, httpMetadata));
                    return Optional.of(httpMetadata);

                default:
                    throw new UnsupportedOperationException("Unexpected bundle type: " + bundle.getType());
            }
        } catch (IOException e) {
            issues.add(new ValidationException(bundle, "Loading of bundle metadata failed: " + metadataPath, e));
            return Optional.empty();
        }
    }

    private List<ValidationException> checkMandatoryFiles(FsBundle bundle, List<Path> bundleFiles) {
        return FsConstants.BUNDLE_MANDATORY_FILES
                .stream()
                .filter(path -> !bundleFiles.contains(path))
                .map(path -> new ValidationException(bundle, "Mandatory file is missing: " + path))
                .collect(Collectors.toList());
    }

    private List<ValidationException> checkUnknownFiles(FsBundle bundle, List<Path> bundleFiles) {
        HashSet<Path> copy = new HashSet<>(bundleFiles);
        copy.removeAll(FsConstants.BUNDLE_ALLOWED_FILES);

        return copy.stream()
                .map(path -> new ValidationException(bundle, "Bundle contains an unexpected file: " + path))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("DuplicatedCode")  // Refactoring would make the code much less readable
    private List<ValidationException> validateDipMetadata(FsBundle bundle, DipMetadata metadata) {
        List<ValidationException> issues = new ArrayList<>();

        validateFormat(bundle, ID_PATTERN, "id", metadata.getId()).ifPresent(issues::add);
        validateFormat(bundle, DATE_PATTERN, "created", metadata.getCreated()).ifPresent(issues::add);
        validateFormat(bundle, VERSION_PATTERN, "version", metadata.getVersion()).ifPresent(issues::add);
        validateFormat(bundle, VERSION_PATTERN, "masVersion", metadata.getMasVersion()).ifPresent(issues::add);

        validateSync(bundle, bundle::getType, "type", metadata.getType()).ifPresent(issues::add);
        validateSync(bundle, bundle::getVendor, "vendor", metadata.getVendor()).ifPresent(issues::add);
        validateSync(bundle, bundle::getId, "id", metadata.getId()).ifPresent(issues::add);
        bundle.getVersion()
                .flatMap(version -> validateSync(bundle, () -> version, "version", metadata.getVersion()))
                .ifPresent(issues::add);

        validateLanguages(bundle, metadata.getI18nLanguages()).ifPresent(issues::add);

        // TODO: Rules for other validations.

        return issues;
    }

    @SuppressWarnings("DuplicatedCode")  // Refactoring would make the code much less readable
    private List<ValidationException> validateHttpMetadata(FsBundle bundle, HttpMetadata metadata) {
        List<ValidationException> issues = new ArrayList<>();

        validateFormat(bundle, DATE_PATTERN, "created", metadata.getCreated()).ifPresent(issues::add);
        validateFormat(bundle, VERSION_PATTERN, "masVersion", metadata.getMasVersion()).ifPresent(issues::add);

        validateSync(bundle, bundle::getType, "type", metadata.getType()).ifPresent(issues::add);
        validateSync(bundle, bundle::getVendor, "vendor", metadata.getVendor()).ifPresent(issues::add);
        validateSync(bundle, bundle::getId, "id", metadata.getId().toString()).ifPresent(issues::add);

        validateLanguages(bundle, metadata.getI18nLanguages()).ifPresent(issues::add);

        // TODO: Rules for other validations.

        return issues;
    }

    /**
     * Validate that a value matches its expected format.
     */
    private Optional<ValidationException> validateFormat(FsBundle bundle,
                                                         Pattern pattern,
                                                         String field,
                                                         String value) {
        if (pattern.matcher(value).matches()) {
            return Optional.empty();
        }

        return validationIssue(bundle,
                String.format("Invalid value: field `%s`, value `%s`, pattern `%s`", field, value, pattern));
    }

    /**
     * Validate that value in metadata file matches name of directory in filesystem tree.
     */
    private <T> Optional<ValidationException> validateSync(FsBundle bundle,
                                                           Supplier<T> valueSupplier,
                                                           String field,
                                                           T value) {
        T fsValue = valueSupplier.get();
        if (fsValue.equals(value)) {
            return Optional.empty();
        }

        return validationIssue(bundle,
                String.format("Values mismatch: field `%s`, filesystem `%s` != metadata `%s`", field, fsValue, value));
    }

    private Optional<ValidationException> validateLanguages(FsBundle bundle, List<String> languages) {
        Path transDir = bundle.getPath().resolve(FsConstants.TRANSLATIONS_DIR);

        List<String> languagesMetadata = languages
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> languagesFs = bundle.getFiles()
                .stream()
                .filter(path -> path.startsWith(FsConstants.TRANSLATIONS_DIR))
                .map(transDir::relativize)
                .map(path -> path.getFileName().toString().replace(".json", ""))
                .collect(Collectors.toList());

        if (!languagesMetadata.equals(languagesFs)) {
            return validationIssue(bundle,
                    String.format("Values mismatch: field `i18nLanguages`, filesystem `%s` != metadata `%s`",
                            languagesFs, languagesMetadata));
        }

        return Optional.empty();
    }

    private Optional<ValidationException> validationIssue(FsBundle bundle, String message) {
        return Optional.of(new ValidationException(bundle, message));
    }
}
