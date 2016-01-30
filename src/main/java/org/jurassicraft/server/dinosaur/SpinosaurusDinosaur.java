package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.SpinosaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class SpinosaurusDinosaur extends Dinosaur
{
    public SpinosaurusDinosaur()
    {
        super();

        this.setName("Spinosaurus");
        this.setDinosaurClass(SpinosaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x48403D, 0xC5CFDA);
        this.setEggColorFemale(0x756862, 0x91594D);
        this.setHealth(10, 90);
        this.setSpeed(0.46, 0.80);
        this.setStrength(5, 40);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.6F, 3.8F);
        this.setSizeX(0.6F, 3.0F);
        this.setSizeY(0.8F, 4.8F);
        this.setStorage(54);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.37F, 0.3F);
    }
}
