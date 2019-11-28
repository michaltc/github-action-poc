package com.citrix.microapps.bundlegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.citrix.microapps.bundlegen.bundles.FindBundles;

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

        if (!Files.isDirectory(bundlesDir) || !Files.isReadable(bundlesDir)) {
            throw new ValidationException("Input path with bundles does not exist or is not a readable directory: " + bundlesDir);
        }

        try {
            Files.createDirectories(distDir);
        } catch (IOException e) {
            throw new ValidationException("Creation of output directory failed: " + distDir, e);
        }

        new FindBundles()
                .findBundles(bundlesDir)
                .forEach(bundle -> System.out.println("Bundle: " + bundle));
    }
}
