package com.citrix.microapps.bundlegen.bundles;

import java.nio.file.Path;
import java.util.Optional;

/**
 * One HTTP bundle located in filesystem.
 * <p>
 * The structure of directories is `.../vendor/bundle ID/...`.
 */
public class FsHttpBundle implements FsBundle {
    private final Path path;

    public FsHttpBundle(Path path) {
        this.path = path;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getVendor() {
        return path.getParent().getFileName().toString();
    }

    @Override
    public String getId() {
        return path.getFileName().toString();
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.empty();
    }

    @Override
    public String getArchiveName() {
        return getVendor() + "_" + getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsHttpBundle fsBundle = (FsHttpBundle) o;

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
