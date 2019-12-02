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

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Builder of zip archive with bundle. Produce always exactly same zip on byte level for the same input, any difference
 * would cause unwanted growing of git repository with archives, invalidating of possible HTTP proxy caches in CDN,
 * files re-downloading, etc.
 */
public class ArchiveBuilder {
    private static final FileTime EPOCH = FileTime.fromMillis(0);

    public static FsBundle buildAndStore(Path archivesDir, FsBundle bundle) {
        ArchiveBuilder builder = new ArchiveBuilder();
        byte[] content = builder.buildArchive(bundle);
        return builder.storeArchive(archivesDir, bundle, content);
    }

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

    /**
     * Store bundle archive to the file system on path like `archivesDir/vendor/vendor_bundleID.zip`.
     * <p>
     * Each bundle is stored always to the same location which effectively overwrites its previous version on update.
     * Don't use anything like MD5 or timestamp in the path.
     * <p>
     * Expecting no `firstLetter/secondLetter/thirdLetter` subdirectories are needed. `vendor` subdirectory should be
     * enough to have max. tens or small hundreds of directories/files per directory on any level.
     *
     * @param archivesDir top level directory for all archives
     * @param bundle      bundle to store
     * @param content     content of the bundle archive
     * @return the bundle passed in
     */
    public FsBundle storeArchive(Path archivesDir, FsBundle bundle, byte[] content) {
        Path archivePath = bundle.getArchivePath(archivesDir);

        try {
            Files.createDirectories(archivePath.getParent());
            Files.write(archivePath, content, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Storing of zip archive to file system failed: " + archivePath, e);
        }

        return bundle;
    }

    private void addToArchive(ZipOutputStream zipStream, String archiveName, Path topDirectory, Path file) {
        try {
            String relativePath = archiveName + "/" + topDirectory.relativize(file);

            // Git unfortunately doesn't preserve the times, it uses current time on checkout of every file it modifies.
            // Current time would be used in zip if the times were not defined, see putNextEntry(). `git log` can be
            // used to get the timestamps if really needed.
            // https://git.wiki.kernel.org/index.php/GitFaq#Why_isn.27t_Git_preserving_modification_time_on_files.3F
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
