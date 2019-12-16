package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.citrix.microapps.bundlegen.pojo.Bundles;
import com.citrix.microapps.bundlegen.pojo.MetadataOut;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static com.citrix.microapps.bundlegen.bundles.FsConstants.BUNDLES_JSON;

/**
 * Reader of input bundles and writer of the output ones.
 */
public class BundlesProcessor {
    private static final ObjectWriter METADATA_WRITER = new ObjectMapper()
            .writerWithDefaultPrettyPrinter();

    private final BundlesFinder finder;
    private final BundlesLoader loader;
    private final BundlesArchiver archiver;

    private final Path distDir;
    private final URI bundlesRepository;

    public BundlesProcessor(BundlesFinder finder,
                            BundlesLoader loader,
                            BundlesArchiver archiver,
                            Path distDir,
                            URI bundlesRepository) {
        this.finder = finder;
        this.loader = loader;
        this.archiver = archiver;
        this.distDir = distDir;
        this.bundlesRepository = bundlesRepository;
    }

    public void processAllBundles() {
        List<Bundle> allBundles = finder
                .findDipBundles()
                .map(loader::loadDipBundle)
                .collect(Collectors.toList());

        List<MetadataOut> archivedBundles = allBundles.stream()
                .map(this::processOneBundle)
                .collect(Collectors.toList());

        writeBundlesJson(archivedBundles, distDir.resolve(BUNDLES_JSON));
    }

    public MetadataOut processOneBundle(Bundle bundle) {
        System.out.println("Archiving bundle: " + bundle);
        byte[] content = archiver.buildArchive(bundle.getFs());
        Path archivePath = archiver.storeArchive(bundle.getFs(), content);
        String md5Hex = archiver.md5Hex(content);
        MetadataOut metadataOut = bundle.toBundlesMetadata(bundlesRepository, md5Hex);
        System.out.println("Bundle archived: " + archivePath + ", " + md5Hex);
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
