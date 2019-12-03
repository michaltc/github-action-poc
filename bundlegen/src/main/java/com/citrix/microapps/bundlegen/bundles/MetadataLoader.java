package com.citrix.microapps.bundlegen.bundles;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import com.citrix.microapps.bundlegen.pojo.MetadataIn;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Loader of bundle metadata from `metadata.json`.
 */
public class MetadataLoader {
    private static final ObjectReader METADATA_READER = new ObjectMapper()
            .reader()
            .with(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .with(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
            .with(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
            .forType(MetadataIn.class);

    public static MetadataIn load(FsBundle bundle) {
        try {
            File file = bundle.getMetadataPath().toFile();
            return METADATA_READER.readValue(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Reading of bundle metadata failed: " + bundle, e);
        }
    }
}
