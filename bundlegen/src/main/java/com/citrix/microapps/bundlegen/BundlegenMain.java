package com.citrix.microapps.bundlegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.citrix.microapps.bundlegen.bundles.ArchiveBuilder;
import com.citrix.microapps.bundlegen.bundles.Bundle;
import com.citrix.microapps.bundlegen.bundles.BundlesFinder;
import com.citrix.microapps.bundlegen.bundles.MetadataLoader;

/**
 * Application runner with `main()`.
 */
class BundlegenMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: bundlegen bundles-dir dist-dir");
            System.exit(1);
        }

        Path bundlesDir = Paths.get(args[0]);
        Path distDir = Paths.get(args[1]);
        Path archivesDir = distDir.resolve("archives");

        if (!Files.isDirectory(bundlesDir) || !Files.isReadable(bundlesDir)) {
            throw new ValidationException("Input path with bundles does not exist or is not a readable directory: " + bundlesDir);
        }

        createDirectories(distDir);
        createDirectories(archivesDir);

        new BundlesFinder()
                .findBundles(bundlesDir)
                .map(bundle -> new Bundle(bundle, MetadataLoader.load(bundle)))
                .map(bundle -> ArchiveBuilder.buildAndStore(archivesDir, bundle))
                .forEach(bundle -> System.out.println("Bundle processed: " + bundle));
    }

    private static void createDirectories(Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new ValidationException("Creation of directory failed: " + directory, e);
        }
    }
}
