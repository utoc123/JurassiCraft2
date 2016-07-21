package org.jurassicraft.server.entity.base;

import org.jurassicraft.server.lang.LangHelper;

public enum GrowthStage
{
    INFANT, JUVENILE, ADOLESCENT, ADULT;

    // Enum#values() is not being cached for security reasons. DONT PERFORM CHANGES ON THIS ARRAY
    public static final GrowthStage[] values = GrowthStage.values();

    public String getLocalization()
    {
        return new LangHelper("growth_stage." + name().toLowerCase() + ".name").build();
    }
}
