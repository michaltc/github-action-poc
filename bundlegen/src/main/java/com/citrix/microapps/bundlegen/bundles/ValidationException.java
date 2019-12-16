package com.citrix.microapps.bundlegen.bundles;

/**
 * Bundle catalog doesn't match expected structure or is somehow broken.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(FsBundle bundle, String message) {
        super(bundle + ": " + message);
    }

    public ValidationException(FsBundle bundle, String message, Throwable cause) {
        super(bundle + ": " + message, cause);
    }
}
