package com.citrix.microapps.bundlegen.bundles;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

import com.citrix.microapps.bundlegen.pojo.Type;

import static com.citrix.microapps.bundlegen.bundles.FsConstants.ARCHIVE_EXTENSION;
import static com.citrix.microapps.bundlegen.bundles.FsConstants.METADATA_FILE;

/**
 * Generic bundle located in filesystem.
 * <p>
 * The structure of directories is `.../vendor/id/version/...`.
 */
public interface FsBundle {
    Type getType();

    Path getPath();

    String getVendor();

    String getId();

    Optional<String> getVersion();

    String getArchiveName();

    default Path getArchivePath(Path archivesDir) {
        return archivesDir
                .resolve(getVendor())
                .resolve(getArchiveName() + ARCHIVE_EXTENSION);
    }

    default URI getDownloadUrl(URI bundlesRepository) {
        String repo = bundlesRepository.toString();
        String repoSlash = repo.endsWith("/") ? repo : repo + "/";
        return URI.create(repoSlash + getVendor() + "/" + getArchiveName() + ARCHIVE_EXTENSION);
    }

    default Path getMetadataPath() {
        return getPath().resolve(METADATA_FILE);
    }
}
