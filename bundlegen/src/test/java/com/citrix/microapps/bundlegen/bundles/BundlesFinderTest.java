package com.citrix.microapps.bundlegen.bundles;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static com.citrix.microapps.bundlegen.TestUtils.path;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BundlesFinderTest {
    private List<FsBundle> findBundles(Path path) {
        return new BundlesFinder(path)
                .findBundles()
                .sorted(Comparator.comparing(FsBundle::getPath))
                .collect(Collectors.toList());
    }

    @Test
    void findBundles() {
        List<FsBundle> actual = findBundles(path("src/test/resources/bundles"));

        List<FsBundle> expected = Arrays.asList(
                new FsDipBundle(path("src/test/resources/bundles/dip/vendor1/bundle1/0.0.1")),
                new FsDipBundle(path("src/test/resources/bundles/dip/vendor1/bundle2/0.0.1")),
                new FsDipBundle(path("src/test/resources/bundles/dip/vendor2/bundle1/0.0.1")),
                new FsHttpBundle(path("src/test/resources/bundles/http/vendor2/bundle2"))
        );

        assertEquals(expected, actual);
    }

    @Test
    void unexpectedFileInDirectories() {
        assertThatThrownBy(() -> findBundles(path("src/test/resources/bundles_unexpected_file")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Path is not a directory: ");
    }

    @Test
    void directoryDoesNotExist() {
        assertThatThrownBy(() -> findBundles(path("this/path/does/not/exist")))
                .isInstanceOf(UncheckedIOException.class)
                .hasMessageContaining("Listing of directory failed: ");
    }
}
