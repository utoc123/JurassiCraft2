package org.jurassicraft.server.lang;

import net.minecraft.client.resources.I18n;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class AdvLang
{
    private String langPath;
    private Map<String, String> properties = new HashMap<>();

    public AdvLang(String langPath)
    {
        this.langPath = langPath;
    }

    public AdvLang withProperty(String propertyName, String value)
    {
        properties.put(propertyName, I18n.format(value));

        return this;
    }

    public String build()
    {
        String translation = I18n.format(langPath);

        for (Entry<String, String> property : properties.entrySet())
        {
            translation = translation.replaceAll(Pattern.quote("{" + property.getKey() + "}"), property.getValue());
        }

        return translation;
    }
}
