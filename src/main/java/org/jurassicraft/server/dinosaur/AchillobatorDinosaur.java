package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.AchillobatorEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class AchillobatorDinosaur extends Dinosaur
{
    public AchillobatorDinosaur()
    {
        super();

        this.setName("Achillobator");
        this.setDinosaurClass(AchillobatorEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x7A7268, 0x7E4941);
        this.setEggColorFemale(0xE1DFDC, 0x675C58);
        this.setHealth(10, 40);
        this.setSpeed(0.85, 0.80);
        this.setStorage(27);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.3F, 1.4F);
        this.setSizeY(0.5F, 1.8F);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth");
        this.setHeadCubeName("head");
        this.setScale(1.1F, 0.325F);
        this.disableRegistry();
    }

    @Override
    public boolean useAllGrowthStages()
    {
        return true;
    }
}
