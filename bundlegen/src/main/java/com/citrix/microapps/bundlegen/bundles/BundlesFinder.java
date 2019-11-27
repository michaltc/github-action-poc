package com.citrix.microapps.bundlegen.bundles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
public class BundlesFinder {
    public Stream<FsBundle> findBundles(Path rootDirectory) {
        return listSubdirectories(rootDirectory)    // vendors
                .flatMap(this::listSubdirectories)  // bundle IDs
                .map(FsBundle::new);
    }

    /**
     * List all direct subdirectories of a directory. Fail fast on any non-directory entry.
     */
    private Stream<Path> listSubdirectories(Path directory) {
        // Expecting only tens, max. hundreds subdirectories inside, no real streaming should be needed next years.
        List<Path> result = new ArrayList<>();

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(directory)) {
            for (Path path : paths) {
                if (!Files.isDirectory(path)) {
                    throw new ValidationException("Path is not a directory: " + path);
                }

                result.add(path);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return result.stream();
    }
}
