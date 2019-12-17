package com.citrix.microapps.bundlegen.bundles;

import java.nio.file.Path;
import java.util.Optional;

import com.citrix.microapps.bundlegen.pojo.Type;

/**
 * One DIP bundle located in filesystem.
 * <p>
 * The structure of directories is `.../vendor/bundle ID/version/...`.
 */
public class FsDipBundle implements FsBundle {
    private final Path path;

    public FsDipBundle(Path path) {
        this.path = path;
    }

    @Override
    public Type getType() {
        return Type.DIP;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getVendor() {
        return path.getParent().getParent().getFileName().toString();
    }

    @Override
    public String getId() {
        return path.getParent().getFileName().toString();
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.of(getVersionInternal());
    }

    public String getVersionInternal() {
        return path.getFileName().toString();
    }

    @Override
    public String getArchiveName() {
        return getVendor() + "_" + getId() + "_" + getVersionInternal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsDipBundle fsBundle = (FsDipBundle) o;

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
