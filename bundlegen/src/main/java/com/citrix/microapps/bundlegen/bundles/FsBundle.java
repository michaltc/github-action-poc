package com.citrix.microapps.bundlegen.bundles;

import java.nio.file.Path;

/**
 * One bundle located in file system.
 * <p>
 * The structure of directories is `.../vendor/id/`.
 */
public class FsBundle {
    private static final String ARCHIVE_EXTENSION = ".zip";

    private final Path path;

    public FsBundle(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public String getVendor() {
        return path.getParent().getFileName().toString();
    }

    public String getId() {
        return path.getFileName().toString();
    }

    public String getArchiveName() {
        return getVendor() + "_" + getId();
    }

    public Path getArchivePath(Path archivesDir) {
        return archivesDir
                .resolve(getVendor())
                .resolve(getArchiveName() + ARCHIVE_EXTENSION);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsBundle fsBundle = (FsBundle) o;

        return path.equals(fsBundle.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
