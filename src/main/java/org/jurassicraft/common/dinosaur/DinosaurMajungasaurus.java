package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityMajungasaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurMajungasaurus extends Dinosaur
{
    public DinosaurMajungasaurus()
    {
        super();

        this.setName("Majungasaurus");
        this.setDinosaurClass(EntityMajungasaurus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE6CC9B, 0x7C8A7D);
        this.setEggColorFemale(0xE8CF9C, 0xADAC7E);
        this.setHealth(16, 65);
        this.setStrength(6, 36);
        this.setSpeed(0.48, 0.40);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.6F, 2.6F);
        this.setSizeX(0.5F, 2.25F);
        this.setSizeY(0.8F, 3.0F);
        this.setStorage(36);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth");
        this.setHeadCubeName("Head");
    }
}