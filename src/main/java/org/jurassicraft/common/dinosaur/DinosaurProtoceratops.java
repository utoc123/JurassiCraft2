package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityProtoceratops;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurProtoceratops extends Dinosaur
{
    public DinosaurProtoceratops()
    {
        super();

        this.setName("Protoceratops");
        this.setDinosaurClass(EntityProtoceratops.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xFDCEB5, 0xFBC073);
        this.setEggColorFemale(0xEBCC98, 0xAA804E);
        this.setHealth(16, 55);
        this.setSpeed(0.46, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 0.85F);
        this.setSizeX(0.3F, 1.0F);
        this.setSizeY(0.4F, 1.25F);
        this.setStorage(27);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
    }
}
