package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityPachycephalosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurPachycephalosaurus extends Dinosaur
{
    public DinosaurPachycephalosaurus()
    {
        super();

        this.setName("Pachycephalosaurus");
        this.setDinosaurClass(EntityPachycephalosaurus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD9E1C0, 0x91501F);
        this.setEggColorFemale(0x8E805E, 0x4A5154);
        this.setHealth(16, 55);
        this.setSpeed(0.46, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(40));
        this.setSizeX(0.25F, 1.25F);
        this.setSizeY(0.5F, 2.3F);
        this.setStorage(27);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "teeth");
    }
}
