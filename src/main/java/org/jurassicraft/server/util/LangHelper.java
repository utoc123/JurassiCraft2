package org.jurassicraft.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.minecraft.util.text.translation.I18n;

public class LangHelper {
    private String langPath;
    private Map<String, String> properties = new HashMap<>();

    public LangHelper(String langPath) {
        this.langPath = langPath;
    }

    public LangHelper withProperty(String propertyName, String value) {
        this.properties.put(propertyName, I18n.translateToLocal(value));

        return this;
    }

    public String build() {
        String translation = I18n.translateToLocal(this.langPath);

        for (Entry<String, String> property : this.properties.entrySet()) {
            translation = translation.replaceAll(Pattern.quote("{" + property.getKey() + "}"), property.getValue());
        }

        return translation;
    }
}
