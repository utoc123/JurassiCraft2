package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.LeaellynasauraEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class LeaellynasauraDinosaur extends Dinosaur
{
    public LeaellynasauraDinosaur()
    {
        super();

        this.setName("Leaellynasaura");
        this.setDinosaurClass(LeaellynasauraEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE1D0A6, 0x262B27);
        this.setEggColorFemale(0xC8B50C, 0x926045);
        this.setHealth(3, 10);
        this.setStrength(1, 5);
        this.setSpeed(0.45, 0.50);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.35F, 0.95F);
        this.setSizeX(0.25F, 0.6F);
        this.setSizeY(0.35F, 0.95F);
        this.setStorage(9);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder");
        this.setHeadCubeName("Head ");
        this.setScale(0.7F, 0.25F);
    }
}
