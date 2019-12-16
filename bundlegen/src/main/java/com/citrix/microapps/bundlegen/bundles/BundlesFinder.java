package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.citrix.microapps.bundlegen.bundles.FsConstants.DIP_DIR;

/**
 * Find all bundles in a directory tree with the standard structure.
 */
public class BundlesFinder {
    private static final Logger logger = LoggerFactory.getLogger(BundlesFinder.class);

    private final Path dipsDir;

    public BundlesFinder(Path bundlesDir) {
        this.dipsDir = bundlesDir.resolve(DIP_DIR);
    }

    /**
     * DIP bundles use 3 levels of directories: vendor - bundle - version.
     */
    public Stream<FsBundle> findDipBundles() {
        logger.info("Searching for all DIP bundles: {}", dipsDir);
        return listDirectSubdirectories(dipsDir)          // vendor
                .flatMap(this::listDirectSubdirectories)  // bundle ID
                .flatMap(this::listDirectSubdirectories)  // version
                .map(FsBundle::new);
    }

    /**
     * List all direct subdirectories of a directory. Fail fast on any non-directory entry.
     */
    private Stream<Path> listDirectSubdirectories(Path directory) {
        try (Stream<Path> paths = Files.walk(directory, 1)) {
            return paths
                    .filter(path -> !directory.equals(path))
                    .filter(this::isDirectoryOrException)
                    // Process the subdirectories always in the same order, less differences for git.
                    .sorted()
                    // Streams are lazily evaluated so we need to first collect all the paths, close the opened
                    // directory to prevent resource leak and then build a new stream from the intermediate list.
                    // Expecting only tens, max. hundreds subdirectories inside, no real streaming should be needed
                    // during next years.
                    .collect(Collectors.toList())
                    .stream();
        } catch (IOException e) {
            throw new UncheckedIOException("Listing of directory failed: " + directory, e);
        }
    }

    private boolean isDirectoryOrException(Path path) {
        if (Files.isDirectory(path)) {
            return true;
        } else {
            throw new RuntimeException("Path is not a directory: " + path);
        }
    }
}
