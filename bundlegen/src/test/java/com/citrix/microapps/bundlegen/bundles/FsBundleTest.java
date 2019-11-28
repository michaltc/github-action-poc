package com.citrix.microapps.bundlegen.bundles;

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
        FsBundle fsBundle = new FsBundle(path);

        assertAll(
                () -> assertSame(path, fsBundle.getPath()),
                () -> assertEquals("vendor", fsBundle.getVendor()),
                () -> assertEquals("id", fsBundle.getId()),
                () -> assertEquals("vendor_id", fsBundle.getArchiveName()),
                () -> assertEquals("test/vendor/id", fsBundle.toString())
        );
    }
}
