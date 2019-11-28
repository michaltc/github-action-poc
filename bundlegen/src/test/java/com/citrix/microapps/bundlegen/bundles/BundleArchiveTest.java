package com.citrix.microapps.bundlegen.bundles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.Test;

import static com.citrix.microapps.bundlegen.TestUtils.path;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BundleArchiveTest {
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

    @Test
    void buildArchive() {
        FsBundle bundle = new FsBundle(path("src/test/resources/bundles/vendor1/bundle1"));
        byte[] content = new BundleArchive().buildArchive(bundle);

        byte[] expectedChecksum = new byte[] {
                -102, 91, -81, 124, -75, -67, -58, -75,
                -52, 11, 79, 42, 55, -69, -47, -96};

        List<String> expectedEntries = Arrays.asList(
                "vendor1_bundle1/i18n/de.json",
                "vendor1_bundle1/i18n/en.json",
                "vendor1_bundle1/i18n/es.json",
                "vendor1_bundle1/i18n/fr.json",
                "vendor1_bundle1/i18n/ja.json",
                "vendor1_bundle1/i18n/nl.json",
                "vendor1_bundle1/i18n/zh-CN.json",
                "vendor1_bundle1/metadata.json"
        );

        assertAll(
                () -> assertArrayEquals(expectedChecksum, MessageDigest.getInstance("MD5").digest(content),
                        "Produced zip should be always exactly same on byte level"),
                () -> assertEquals(expectedEntries, listEntriesInZip(content))
        );
    }

    @Test
    void directoryDoesNotExist() {
        FsBundle bundle = new FsBundle(path("this/path/does/not/exist"));

        assertThatThrownBy(() -> new BundleArchive().buildArchive(bundle))
                .isInstanceOf(UncheckedIOException.class)
                .hasMessageContaining("Creation of zip archive failed: ");
    }
}
