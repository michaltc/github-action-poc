package com.citrix.microapps.bundlegen.bundles;

import java.net.URI;
import java.nio.file.Path;

import static com.citrix.microapps.bundlegen.bundles.FsConstants.ARCHIVE_EXTENSION;
import static com.citrix.microapps.bundlegen.bundles.FsConstants.METADATA_FILE;

/**
 * One DIP bundle located in filesystem.
 * <p>
 * The structure of directories is `.../vendor/id/version/...`.
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
        return path.getParent().getParent().getFileName().toString();
    }

    public String getId() {
        return path.getParent().getFileName().toString();
    }

    public String getVersion() {
        return path.getFileName().toString();
    }

    public String getArchiveName() {
        return getVendor() + "_" + getId() + "_" + getVersion();
    }

    public Path getArchivePath(Path archivesDir) {
        return archivesDir
                .resolve(getVendor())
                .resolve(getArchiveName() + ARCHIVE_EXTENSION);
    }

    public URI getDownloadUrl(URI bundlesRepository) {
        String repo = bundlesRepository.toString();
        String repoSlash = repo.endsWith("/") ? repo : repo + "/";
        return URI.create(repoSlash + getVendor() + "/" + getArchiveName() + ARCHIVE_EXTENSION);
    }

    public Path getMetadataPath() {
        return path.resolve(METADATA_FILE);
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
