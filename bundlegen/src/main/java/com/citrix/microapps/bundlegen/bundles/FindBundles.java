package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.citrix.microapps.bundlegen.ValidationException;

/**
 * Find all bundles in a directory tree with the standard structure.
 *
 * <pre>
 * root
 *   - vendor
 *      - bundle
 *      - bundle
 *   - vendor
 *      - bundle
 * </pre>
 */
public class FindBundles {
    public Stream<FsBundle> findBundles(Path rootDirectory) {
        return listDirectSubdirectories(rootDirectory)    // vendors
                .flatMap(this::listDirectSubdirectories)  // bundle IDs
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
            throw new ValidationException("Path is not a directory: " + path);
        }
    }
}
