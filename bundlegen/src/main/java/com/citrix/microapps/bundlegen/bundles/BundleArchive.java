package com.citrix.microapps.bundlegen.bundles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Builder of zip archive with bundle. Produce always exactly same zip on byte level for the same input, any difference
 * would cause unwanted growing of git repository with archives, invalidating of possible HTTP proxy caches in CDN,
 * files re-downloading, etc.
 */
public class BundleArchive {
    private static final FileTime EPOCH = FileTime.fromMillis(0);

    /**
     * Build zip archive with all the files of the bundle.
     */
    public byte[] buildArchive(FsBundle bundle) {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ZipOutputStream zipStream = new ZipOutputStream(bytes)) {
            zipStream.setMethod(ZipEntry.DEFLATED);
            zipStream.setLevel(Deflater.BEST_COMPRESSION);

            String archiveName = bundle.getArchiveName();

            try (Stream<Path> paths = Files.walk(bundle.getPath())) {
                // Empty directories are intentionally ignored while traversing, they will be missing in the archive.
                // Git can't store them and the archives would be only a little bigger with no benefit. If you decide
                // to have also directory entries in zip, directory entry is defined to be one whose name ends with a
                // '/' and have no content.
                paths.filter(Files::isRegularFile)
                        // Make sure the files are always iterated and added to zip in the same order.
                        .sorted()
                        .forEach(file -> addToArchive(zipStream, archiveName, bundle.getPath(), file));
            }

            zipStream.finish();
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Creation of zip archive failed: " + bundle.getPath(), e);
        }
    }

    private void addToArchive(ZipOutputStream zipStream, String archiveName, Path topDirectory, Path file) {
        try {
            String relativePath = archiveName + "/" + topDirectory.relativize(file);

            // Git unfortunately doesn't preserve the times, it uses current time on clone (experimentally verified).
            // Current time would be used in ZIP if the times were not defined, see putNextEntry().
            ZipEntry entry = new ZipEntry(relativePath)
                    .setCreationTime(EPOCH)
                    .setLastAccessTime(EPOCH)
                    .setLastModifiedTime(EPOCH);

            zipStream.putNextEntry(entry);
            Files.copy(file, zipStream);
            zipStream.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException("Adding of file to zip archive failed: " + file, e);
        }
    }
}
