package com.citrix.microapps.bundlegen.pojo;

import java.util.List;

/**
 * Data to be written to `bundles.json`.
 */
public class Bundles {
    private final List<MetadataOut> bundles;

    public Bundles(List<MetadataOut> bundles) {
        this.bundles = bundles;
    }

    public List<MetadataOut> getBundles() {
        return bundles;
    }
}
