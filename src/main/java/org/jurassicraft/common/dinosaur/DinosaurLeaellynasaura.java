package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityLeaellynasaura;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurLeaellynasaura extends Dinosaur
{
    public DinosaurLeaellynasaura()
    {
        super();

        this.setName("Leaellynasaura");
        this.setDinosaurClass(EntityLeaellynasaura.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE1D0A6, 0x262B27);
        this.setEggColorFemale(0xC8B50C, 0x926045);
        this.setHealth(10, 25);
        this.setStrength(6, 36);
        this.setSpeed(0.45, 0.50);
        this.setMaximumAge(35);
        this.setEyeHeight(0.35F, 0.95F);
        this.setSizeX(0.25F, 0.6F);
        this.setSizeY(0.35F, 0.95F);
        this.setStorage(9);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder");
    }
}
