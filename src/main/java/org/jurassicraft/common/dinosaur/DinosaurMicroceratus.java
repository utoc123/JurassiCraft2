package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityMicroceratus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurMicroceratus extends Dinosaur
{
    public DinosaurMicroceratus()
    {
        super();

        this.setName("Microceratus");
        this.setDinosaurClass(EntityMicroceratus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x956F2D, 0x92442C);
        this.setEggColorFemale(0x958331, 0x7E4A1F);
        this.setHealth(10, 25);
        this.setSpeed(0.41, 0.35);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.14F, 0.36F);
        this.setSizeX(0.15F, 0.4F);
        this.setSizeY(0.18F, 0.55F);
        this.setStorage(9);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
    }
}
