package org.jurassicraft.server.api;

import org.jurassicraft.server.entity.base.EnumGrowthStage;

public class GrowthStageGenderContainer
{
    public EnumGrowthStage growthStage;
    public boolean isMale;

    public GrowthStageGenderContainer(EnumGrowthStage stage, boolean isMale)
    {
        this.growthStage = stage;
        this.isMale = isMale;
    }

    public EnumGrowthStage getGrowthStage()
    {
        return growthStage;
    }

    public boolean isMale()
    {
        return isMale;
    }

    public boolean isFemale()
    {
        return !isMale();
    }

    @Override
    public int hashCode()
    {
        return growthStage.ordinal() * (isMale() ? 1 : 0) * 54;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof GrowthStageGenderContainer)
        {
            GrowthStageGenderContainer container = (GrowthStageGenderContainer) o;

            return container.growthStage == growthStage && container.isMale == isMale;
        }

        return false;
    }
}
