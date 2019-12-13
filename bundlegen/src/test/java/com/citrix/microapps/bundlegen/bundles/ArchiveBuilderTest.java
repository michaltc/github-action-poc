package com.citrix.microapps.bundlegen.bundles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static com.citrix.microapps.bundlegen.TestUtils.path;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArchiveBuilderTest {
    private static final FsBundle TEST_BUNDLE =
            new FsBundle(path("src/test/resources/bundles/dip/vendor1/bundle1/0.0.1"));

    private List<String> listEntriesInZip(byte[] content) throws IOException {
        List<String> result = new ArrayList<>();

        try (ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(content))) {
            ZipEntry entry = zipStream.getNextEntry();
            while (entry != null) {
                result.add(entry.getName());
                entry = zipStream.getNextEntry();
            }
        }

        return result;
    }

    /**
     * @see #TEST_BUNDLE
     */
    private void assertContent(byte[] content) {
        List<String> expectedEntries = Arrays.asList(
                "vendor1_bundle1_0.0.1/i18n/de.json",
                "vendor1_bundle1_0.0.1/i18n/en.json",
                "vendor1_bundle1_0.0.1/i18n/es.json",
                "vendor1_bundle1_0.0.1/i18n/fr.json",
                "vendor1_bundle1_0.0.1/i18n/ja.json",
                "vendor1_bundle1_0.0.1/i18n/nl.json",
                "vendor1_bundle1_0.0.1/i18n/zh-CN.json",
                "vendor1_bundle1_0.0.1/metadata.json"
        );

        assertAll(
                () -> assertEquals("7ea289396d9ae1526a74160a3d6a0ba1", new ArchiveBuilder().md5Hex(content),
                        "Produced zip should be always exactly same on byte level"),
                () -> assertEquals(expectedEntries, listEntriesInZip(content))
        );
    }

    @Test
    void buildArchive() {
        byte[] content = new ArchiveBuilder().buildArchive(TEST_BUNDLE);
        assertContent(content);
    }

    @Test
    void directoryDoesNotExist() {
        FsBundle bundle = new FsBundle(path("this/path/does/not/exist"));

        assertThatThrownBy(() -> new ArchiveBuilder().buildArchive(bundle))
                .isInstanceOf(UncheckedIOException.class)
                .hasMessageContaining("Creation of zip archive failed: ");
    }

    @Test
    void buildAndStore(@TempDir Path tempDir) throws Exception {
        ArchiveBuilder builder = new ArchiveBuilder();
        byte[] content = builder.buildArchive(TEST_BUNDLE);
        Path path = builder.storeArchive(tempDir, TEST_BUNDLE, content);

        assertEquals(tempDir.resolve("vendor1").resolve("vendor1_bundle1_0.0.1.zip"), path);
        assertTrue(Files.exists(path), "Path should exist: " + path);

        byte[] loadedContent = Files.readAllBytes(path);
        assertContent(loadedContent);
    }

    @Test
    void overwriteExisting(@TempDir Path tempDir) throws Exception {
        // No exception should occur if the file is already there.
        buildAndStore(tempDir);
        buildAndStore(tempDir);
        buildAndStore(tempDir);
    }
}
