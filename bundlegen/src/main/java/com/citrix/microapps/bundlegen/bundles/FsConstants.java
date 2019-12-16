package com.citrix.microapps.bundlegen.bundles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Names of standard files and directories in filesystem.
 */
public class FsConstants {
    public static final String DIP_DIR = "dip";
    public static final String METADATA_FILE = "metadata.json";
    public static final String TRANSLATIONS_DIR = "i18n";

    public static final String ARCHIVES_DIR = "archives";
    public static final String ARCHIVE_EXTENSION = ".zip";
    public static final String BUNDLES_JSON = "bundles.json";

    /**
     * Only these files and directories are allowed in the bundle.
     */
    public static final Set<Path> BUNDLE_ENTRIES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Paths.get(METADATA_FILE),
            Paths.get("template.sapp"),
            Paths.get(TRANSLATIONS_DIR),
            Paths.get(TRANSLATIONS_DIR, "de.json"),
            Paths.get(TRANSLATIONS_DIR, "en.json"),
            Paths.get(TRANSLATIONS_DIR, "es.json"),
            Paths.get(TRANSLATIONS_DIR, "fr.json"),
            Paths.get(TRANSLATIONS_DIR, "ja.json"),
            Paths.get(TRANSLATIONS_DIR, "nl.json"),
            Paths.get(TRANSLATIONS_DIR, "zh-CN.json")
    )));
}
