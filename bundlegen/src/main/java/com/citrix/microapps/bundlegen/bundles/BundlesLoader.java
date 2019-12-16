package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citrix.microapps.bundlegen.pojo.MetadataIn;
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
            .with(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
            .forType(MetadataIn.class);

    public Bundle loadDipBundle(FsBundle bundle) {
        logger.info("Loading bundle: {}", bundle);
        List<ValidationException> issues = new ArrayList<>();

        try {
            Set<Path> bundleFiles = listFiles(bundle.getPath());
            issues.addAll(checkMandatoryFiles(bundle, bundleFiles));
            issues.addAll(checkUnknownFiles(bundle, bundleFiles));
        } catch (IOException e) {
            issues.add(new ValidationException(bundle, "Listing of bundle files failed: " + bundle.getPath(), e));
        }

        Optional<MetadataIn> metadata = loadMetadata(issues, bundle);
        issues.addAll(metadata.map(m -> m.validate(bundle)).orElseGet(Collections::emptyList));
        return new Bundle(bundle, metadata, issues);
    }

    private Optional<MetadataIn> loadMetadata(List<ValidationException> issues, FsBundle bundle) {
        Path metadataPath = bundle.getMetadataPath();
        logger.info("Loading bundle metadata: {}", metadataPath);

        try {
            MetadataIn metadata = METADATA_READER.readValue(metadataPath.toFile());
            return Optional.of(metadata);
        } catch (IOException e) {
            issues.add(new ValidationException(bundle, "Loading of bundle metadata failed: " + metadataPath, e));
            return Optional.empty();
        }
    }

    private Set<Path> listFiles(Path directory) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            return paths
                    .filter(path -> Files.isRegularFile(path))
                    .map(directory::relativize)
                    .collect(Collectors.toSet());
        }
    }

    private List<ValidationException> checkMandatoryFiles(FsBundle bundle, Set<Path> bundleFiles) {
        return FsConstants.BUNDLE_MANDATORY_FILES
                .stream()
                .filter(path -> !bundleFiles.contains(path))
                .map(path -> new ValidationException(bundle, "Mandatory file is missing: " + path))
                .collect(Collectors.toList());
    }

    private List<ValidationException> checkUnknownFiles(FsBundle bundle, Set<Path> bundleFiles) {
        HashSet<Path> copy = new HashSet<>(bundleFiles);
        copy.removeAll(FsConstants.BUNDLE_ALLOWED_FILES);

        return copy.stream()
                .map(path -> new ValidationException(bundle, "Bundle contains an unexpected file: " + path))
                .collect(Collectors.toList());
    }
}
