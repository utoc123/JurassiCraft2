package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.OthnieliaEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class OthnieliaDinosaur extends Dinosaur
{
    public OthnieliaDinosaur()
    {
        super();

        this.setName("Othnielia");
        this.setDinosaurClass(OthnieliaEntity.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0x3EA999, 0x584F41);
        this.setEggColorFemale(0xC9AC95, 0x46342E);
        this.setHealth(10, 28);
        this.setSpeed(0.4, 0.35);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(25));
        this.setEyeHeight(0.2F, 0.55F);
        this.setSizeX(0.15F, 0.4F);
        this.setSizeY(0.25F, 0.55F);
        this.setStorage(9);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head ");
        this.setScale(0.35F, 0.15F);
    }
}
