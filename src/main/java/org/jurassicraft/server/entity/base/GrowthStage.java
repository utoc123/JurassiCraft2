package org.jurassicraft.server.entity.base;

public enum GrowthStage
{
    INFANT, JUVENILE, ADOLESCENT, ADULT;

    // Enum#values() is not being cached for security reasons. DONT PERFORM CHANGES ON THIS ARRAY
    public static final GrowthStage[] values = GrowthStage.values();
}
