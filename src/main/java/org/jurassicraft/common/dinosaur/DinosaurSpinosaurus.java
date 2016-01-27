package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntitySpinosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurSpinosaurus extends Dinosaur
{
    public DinosaurSpinosaurus()
    {
        super();

        this.setName("Spinosaurus");
        this.setDinosaurClass(EntitySpinosaurus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x48403D, 0xC5CFDA);
        this.setEggColorFemale(0x756862, 0x91594D);
        this.setHealth(20, 100);
        this.setSpeed(0.46, 0.80);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.6F, 3.8F);
        this.setSizeX(0.6F, 3.0F);
        this.setSizeY(0.8F, 4.8F);
        this.setStorage(54);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth");
        this.setHeadCubeName("Head");
    }
}
