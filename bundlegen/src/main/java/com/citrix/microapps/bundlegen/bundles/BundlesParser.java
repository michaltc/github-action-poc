package com.citrix.microapps.bundlegen.bundles;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.citrix.microapps.bundlegen.ValidationException;
import com.citrix.microapps.bundlegen.pojo.MetadataIn;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class BundlesParser {
    private static final ObjectReader METADATA_READER = new ObjectMapper()
            .reader()
            .with(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .with(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
            .with(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
            .forType(MetadataIn.class);

    public MetadataIn load(FsBundle bundle) {
        try {
            File file = bundle.getMetadataPath().toFile();
            return METADATA_READER.readValue(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Reading of bundle metadata failed: " + bundle, e);
        }
    }

    private boolean areBundleFilesValidOrException(Path directory) {
        try (Stream<Path> paths = Files.walk(directory)) {
            List<Path> entries = paths
                    .filter(path -> isValidBundleEntry(directory, path))
                    // Streams are lazily evaluated so we need to first collect all the paths, close the opened
                    // directory to prevent resource leak and then build a new stream from the intermediate list.
                    // Expecting only tens, max. hundreds subdirectories inside, no real streaming should be needed
                    // during next years.
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Listing of directory failed: " + directory, e);
        }

        return true;
    }

    private boolean isValidBundleEntry(Path directory, Path path) {
        Path relativePath = directory.relativize(path);

        if (path.equals(directory) || FsConstants.BUNDLE_ENTRIES.contains(relativePath)) {
            return true;
        } else {
            throw new ValidationException("Bundle contains unexpected file/directory: " + path);
        }
    }
}
