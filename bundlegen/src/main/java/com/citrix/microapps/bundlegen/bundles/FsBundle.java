package com.citrix.microapps.bundlegen.bundles;

import java.nio.file.Path;

/**
 * One bundle located in file system.
 * <p>
 * The structure of directories is `.../vendor/id/`.
 */
public class FsBundle {
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

    @Override
    public String toString() {
        return path.toString();
    }
}
