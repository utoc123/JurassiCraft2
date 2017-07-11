package org.jurassicraft.server.entity;

import org.jurassicraft.server.util.LangHelper;

public enum GrowthStage {
    INFANT, JUVENILE, ADOLESCENT,SKELETON, ADULT;

    // Enum#values() is not being cached for security reasons. DONT PERFORM CHANGES ON THIS ARRAY
    public static final GrowthStage[] values = GrowthStage.values();

    public String getLocalization() {
        return new LangHelper("growth_stage." + this.name().toLowerCase() + ".name").build();
    }
}
