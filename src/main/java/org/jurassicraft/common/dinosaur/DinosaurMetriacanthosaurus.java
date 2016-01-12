package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityMetriacanthosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurMetriacanthosaurus extends Dinosaur
{
    public DinosaurMetriacanthosaurus()
    {
        super();

        this.setName("Metriacanthosaurus");
        this.setDinosaurClass(EntityMetriacanthosaurus.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0xB05E1C, 0xE7DB27);
        this.setEggColorFemale(0xB5985E, 0x60451C);
        this.setHealth(16, 5);
        this.setSpeed(0.44, 0.4);
        this.setStrength(12, 36);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 1.75F);
        this.setSizeX(0.25F, 1.15F);
        this.setSizeY(0.35F, 1.95F);
        this.setStorage(27);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth");
    }
}
