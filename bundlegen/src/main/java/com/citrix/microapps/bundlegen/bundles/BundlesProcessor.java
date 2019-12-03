package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;

import com.citrix.microapps.bundlegen.pojo.Bundles;
import com.citrix.microapps.bundlegen.pojo.MetadataIn;
import com.citrix.microapps.bundlegen.pojo.MetadataOut;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Reader of input bundles and writer of the output ones.
 */
public class BundlesProcessor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public MetadataOut processOneBundle(FsBundle fs, Path archivesDir, URI bundlesRepository) {
        System.out.println("Processing bundle: " + fs);
        MetadataIn metadata = MetadataLoader.load(fs);
        Bundle bundle = new Bundle(fs, metadata);
        ArchiveBuilder builder = new ArchiveBuilder();
        byte[] content = builder.buildArchive(bundle.getFs());
        Path archivePath = builder.storeArchive(archivesDir, bundle.getFs(), content);
        String md5Hex = builder.md5Hex(content);
        MetadataOut metadataOut = bundle.toBundlesMetadata(bundlesRepository, md5Hex);
        System.out.println("Bundle processed: " + archivePath + ", " + md5Hex);
        return metadataOut;
    }

    public void writeBundlesJson(List<MetadataOut> allBundles, Path bundlesJson) {
        try {
            Bundles bundles = new Bundles(allBundles);
            OBJECT_MAPPER.writeValue(bundlesJson.toFile(), bundles);
            System.out.println("Metadata generated: " + bundlesJson);
        } catch (IOException e) {
            throw new UncheckedIOException("Writing bundles JSON failed", e);
        }
    }
}
