package com.citrix.microapps.bundlegen.pojo;

import java.util.List;

/**
 * Data to be written to `bundles.json`.
 */
public class Bundles {
    private final List<MetadataOut> bundles;
    private final long exportedTimestamp;

    public Bundles(List<MetadataOut> bundles, long exportedTimestamp) {
        this.bundles = bundles;
        this.exportedTimestamp = exportedTimestamp;
    }

    public List<MetadataOut> getBundles() {
        return bundles;
    }

    public long getExportedTimestamp() {
        return exportedTimestamp;
    }
}
