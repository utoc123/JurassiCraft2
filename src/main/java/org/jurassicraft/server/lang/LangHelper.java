package org.jurassicraft.server.lang;

import net.minecraft.util.text.translation.I18n;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class LangHelper
{
    private String langPath;
    private Map<String, String> properties = new HashMap<>();

    public LangHelper(String langPath)
    {
        this.langPath = langPath;
    }

    public LangHelper withProperty(String propertyName, String value)
    {
        properties.put(propertyName, I18n.translateToLocal(value));

        return this;
    }

    public String build()
    {
        String translation = I18n.translateToLocal(langPath);

        for (Entry<String, String> property : properties.entrySet())
        {
            translation = translation.replaceAll(Pattern.quote("{" + property.getKey() + "}"), property.getValue());
        }

        return translation;
    }
}
