package com.citrix.microapps.bundlegen.pojo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ModelTranslation {

    private Map<String, Map<String, String>> appTranslations;

    @JsonCreator
    public ModelTranslation(Map<String, Map<String, String>> appTranslations) {
        this.appTranslations = appTranslations;
    }

    public Map<String, Map<String, String>> getAppTranslations() {
        return appTranslations;
    }

    public void setAppTranslations(Map<String, Map<String, String>> translations) {
        this.appTranslations = translations;
    }

}
