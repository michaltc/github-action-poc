package com.citrix.microapps.bundlegen.bundles;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.citrix.microapps.bundlegen.ValidationException;

import static com.citrix.microapps.bundlegen.TestUtils.path;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BundlesFinderTest {
    @Test
    void findBundles() {
        List<FsBundle> actual = new BundlesFinder()
                .findBundles(path("src/test/resources/bundles"))
                .sorted(Comparator.comparing(FsBundle::getPath))
                .collect(Collectors.toList());

        List<FsBundle> expected = Arrays.asList(
                new FsBundle(path("src/test/resources/bundles/vendor1/bundle1")),
                new FsBundle(path("src/test/resources/bundles/vendor1/bundle2")),
                new FsBundle(path("src/test/resources/bundles/vendor2/bundle1")),
                new FsBundle(path("src/test/resources/bundles/vendor2/bundle2"))
        );

        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")  // Streams are lazily evaluated, .collect() is needed
    void unexpectedFile() {
        Stream<FsBundle> bundles = new BundlesFinder().findBundles(path("src/test/resources/bundles_unexpected_file"));
        assertThatThrownBy(() -> bundles.collect(Collectors.toList()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Path is not a directory: ");
    }

    @Test
    void directoryDoesNotExist() {
        assertThatThrownBy(() -> new BundlesFinder().findBundles(path("this/path/does/not/exist")))
                .isInstanceOf(UncheckedIOException.class)
                .hasMessageContaining("Listing of directory failed: ");
    }
}
