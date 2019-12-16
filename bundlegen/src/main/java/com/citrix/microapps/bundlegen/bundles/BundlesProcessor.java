package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.citrix.microapps.bundlegen.pojo.Bundles;
import com.citrix.microapps.bundlegen.pojo.MetadataIn;
import com.citrix.microapps.bundlegen.pojo.MetadataOut;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static com.citrix.microapps.bundlegen.bundles.FsConstants.ARCHIVES_DIR;
import static com.citrix.microapps.bundlegen.bundles.FsConstants.BUNDLES_JSON;

/**
 * Reader of input bundles and writer of the output ones.
 */
public class BundlesProcessor {
    private static final ObjectWriter METADATA_WRITER = new ObjectMapper()
            .writerWithDefaultPrettyPrinter();

    private final BundlesFinder finder;
    private final BundlesParser parser;

    private final Path distDir;
    private final Path archivesDir;
    private final URI bundlesRepository;

    public BundlesProcessor(BundlesFinder finder,
                            BundlesParser parser,
                            Path distDir,
                            URI bundlesRepository) {
        this.finder = finder;
        this.parser = parser;
        this.distDir = distDir;
        this.archivesDir = distDir.resolve(ARCHIVES_DIR);
        this.bundlesRepository = bundlesRepository;
    }

    public void processAllBundles() {
        List<MetadataOut> allBundles = finder
                .findDipBundles()
                .map(this::processOneBundle)
                .collect(Collectors.toList());

        writeBundlesJson(allBundles, distDir.resolve(BUNDLES_JSON));
    }

    public MetadataOut processOneBundle(FsBundle fs) {
        System.out.println("Processing bundle: " + fs);
        MetadataIn metadata = parser.load(fs);
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
            METADATA_WRITER.writeValue(bundlesJson.toFile(), bundles);
            System.out.println("Metadata generated: " + bundlesJson);
        } catch (IOException e) {
            throw new UncheckedIOException("Writing bundles JSON failed", e);
        }
    }
}
