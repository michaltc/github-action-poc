package com.citrix.microapps.bundlegen.bundles;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class FsBundleTest {
    @Test
    void getters() {
        Path path = Paths.get("test", "vendor", "id");
        FsBundle bundle = new FsBundle(path);

        assertAll(
                () -> assertSame(path, bundle.getPath()),
                () -> assertEquals("vendor", bundle.getVendor()),
                () -> assertEquals("id", bundle.getId()),
                () -> assertEquals("vendor_id", bundle.getArchiveName()),
                () -> assertEquals(Paths.get("somewhere/archives/vendor/vendor_id.zip"),
                        bundle.getArchivePath(Paths.get("somewhere", "archives"))),
                () -> assertEquals(URI.create("https://github.com/michaltc/workspace-microapps-bundles/blob/master" +
                                "/bundles/archives/vendor/vendor_id.zip"),
                        bundle.getArchiveUrl(URI.create("https://github.com/michaltc/workspace-microapps-bundles/blob" +
                                "/master/bundles/archives/"))),
                () -> assertEquals(URI.create("https://github.com/michaltc/workspace-microapps-bundles/blob/master" +
                                "/bundles/archives/vendor/vendor_id.zip"),
                        bundle.getArchiveUrl(URI.create("https://github.com/michaltc/workspace-microapps-bundles/blob" +
                                "/master/bundles/archives"))),
                () -> assertEquals(Paths.get("test/vendor/id/metadata.json"), bundle.getMetadataPath()),
                () -> assertEquals("test/vendor/id", bundle.toString())
        );
    }
}
