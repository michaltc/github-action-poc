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
import static com.citrix.microapps.bundlegen.bundles.FsConstants.HTTP_DIR;

/**
 * Find all bundles in a directory tree with the standard structure.
 */
public class BundlesFinder {
    private static final Logger logger = LoggerFactory.getLogger(BundlesFinder.class);

    private final Path dipRoot;
    private final Path httpRoot;

    public BundlesFinder(Path bundlesDir) {
        this.dipRoot = bundlesDir.resolve(DIP_DIR);
        this.httpRoot = bundlesDir.resolve(HTTP_DIR);
    }

    public Stream<FsBundle> findBundles() {
        return Stream.concat(findDipBundles(), findHttpBundles());
    }

    /**
     * DIP bundles use 3 levels of directories: vendor - bundle ID - version.
     */
    private Stream<FsBundle> findDipBundles() {
        logger.info("Searching for all DIP bundles: {}", dipRoot);
        return listDirectSubdirectories(dipRoot)          // vendor
                .flatMap(this::listDirectSubdirectories)  // bundle ID
                .flatMap(this::listDirectSubdirectories)  // version
                .map(FsDipBundle::new);
    }

    /**
     * HTTP bundles use 2 levels of directories: vendor - bundle ID.
     */
    private Stream<FsBundle> findHttpBundles() {
        logger.info("Searching for all HTTP bundles: {}", httpRoot);
        return listDirectSubdirectories(httpRoot)          // vendor
                .flatMap(this::listDirectSubdirectories)   // bundle ID
                .map(FsHttpBundle::new);
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
