package com.citrix.microapps.bundlegen.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Microapp {
    private final String title;

    @JsonCreator
    public Microapp(@JsonProperty(value = "title", required = true) String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
