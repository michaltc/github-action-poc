package com.citrix.microapps.bundlegen.bundles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.citrix.microapps.bundlegen.bundles.FsConstants.BUNDLE_ALLOWED_FILES;
import static com.citrix.microapps.bundlegen.bundles.FsConstants.BUNDLE_MANDATORY_FILES;
import static com.citrix.microapps.bundlegen.bundles.FsConstants.METADATA_FILE;
import static com.citrix.microapps.bundlegen.bundles.FsConstants.TEMPLATE_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BundlesLoaderTest {
    private static List<Path> toPaths(String... paths) {
        return Stream.of(paths)
                .map(path -> Paths.get(path))
                .collect(Collectors.toList());
    }

    private List<String> toMessages(List<ValidationException> issues) {
        return issues.stream()
                .map(Throwable::getMessage)
                .collect(Collectors.toList());
    }

    private static Stream<Arguments> checkMandatoryFilesOkProvider() {
        return Stream.of(
                Arguments.of(new ArrayList<>(BUNDLE_MANDATORY_FILES)),
                Arguments.of(toPaths(METADATA_FILE, TEMPLATE_FILE)),
                Arguments.of(toPaths(METADATA_FILE, TEMPLATE_FILE, "more.txt", "files.bin"))
        );
    }

    @ParameterizedTest
    @MethodSource("checkMandatoryFilesOkProvider")
    void checkMandatoryFilesOk(List<Path> input) {
        assertTrue(BundlesLoader.checkMandatoryFiles(input).isEmpty());
    }

    private static Stream<Arguments> checkMandatoryFilesIssuesProvider() {
        return Stream.of(
                Arguments.of(toPaths(),
                        Arrays.asList("Missing mandatory file: metadata.json",
                                "Missing mandatory file: template.sapp")),

                Arguments.of(toPaths(METADATA_FILE),
                        Collections.singletonList("Missing mandatory file: template.sapp")),

                Arguments.of(toPaths(TEMPLATE_FILE),
                        Collections.singletonList("Missing mandatory file: metadata.json")),

                Arguments.of(toPaths("other.txt", "files.bin"),
                        Arrays.asList("Missing mandatory file: metadata.json",
                                "Missing mandatory file: template.sapp"))
        );
    }

    @ParameterizedTest
    @MethodSource("checkMandatoryFilesIssuesProvider")
    void checkMandatoryFilesIssues(List<Path> input, List<String> expectedMessages) {
        List<ValidationException> issues = BundlesLoader.checkMandatoryFiles(input);
        assertEquals(expectedMessages, toMessages(issues));
    }

    private static Stream<Arguments> checkUnexpectedFilesOkProvider() {
        return Stream.of(
                Arguments.of(toPaths()),
                Arguments.of(new ArrayList<>(BUNDLE_ALLOWED_FILES)),
                Arguments.of(toPaths(METADATA_FILE)),
                Arguments.of(toPaths(TEMPLATE_FILE)),
                Arguments.of(toPaths("i18n/en.json", "i18n/de.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("checkUnexpectedFilesOkProvider")
    void checkUnexpectedFilesOk(List<Path> input) {
        assertTrue(BundlesLoader.checkUnexpectedFiles(input).isEmpty());
    }

    private static Stream<Arguments> checkUnexpectedFilesIssuesProvider() {
        return Stream.of(
                   Arguments.of(toPaths(METADATA_FILE, "unexpected.txt"),
                        Collections.singletonList("Unexpected file: unexpected.txt")),

                Arguments.of(toPaths(TEMPLATE_FILE, "unexpected.txt"),
                        Collections.singletonList("Unexpected file: unexpected.txt")),

                Arguments.of(toPaths("other.txt", "files.bin"),
                        Arrays.asList("Unexpected file: other.txt",
                                "Unexpected file: files.bin"))
        );
    }

    @ParameterizedTest
    @MethodSource("checkUnexpectedFilesIssuesProvider")
    void checkUnexpectedFilesIssues(List<Path> input, List<String> expectedMessages) {
        List<ValidationException> issues = BundlesLoader.checkUnexpectedFiles(input);
        assertEquals(expectedMessages, toMessages(issues));
    }
}
