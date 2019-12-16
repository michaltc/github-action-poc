package com.citrix.microapps.bundlegen.pojo;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VaResolver {
    private final UUID uuid;

    @JsonCreator
    public VaResolver(
            @JsonProperty(value = "uuid", required = true) UUID uuid
    ) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
