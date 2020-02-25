package com.citrix.microapps.bundlegen.bundles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.citrix.microapps.bundlegen.pojo.ModelTranslation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslationValidatorTest {

    @Test
    void validTranslationFile() {
        final String translationChecksum = "C542C2EB82422FF09EA88ADD64F91283";

        Map<String, Map<String, String>> translations = new HashMap<>();
        translations.put("1", Collections.singletonMap("key1", "value1"));
        ModelTranslation translationModel = new ModelTranslation(translations);

        assertEquals(translationChecksum, new TranslationValidator(translationModel).checksum());
    }
}