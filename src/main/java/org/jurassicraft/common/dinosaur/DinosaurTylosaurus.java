package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityTylosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurTylosaurus extends Dinosaur
{
    public DinosaurTylosaurus()
    {
        super();

        this.setName("Tylosaurus");
        this.setDinosaurClass(EntityTylosaurus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x187D75, 0x15544F);
        this.setEggColorFemale(0x798A8F, 0x101517);
        this.setHealth(20, 95);
        this.setSpeed(0.85, 0.75);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(60));
        this.setEyeHeight(0.35F, 2.35F);
        this.setSizeX(0.85F, 4.5F);
        this.setSizeY(0.55F, 2.95F);
        this.setMarineAnimal(true);
        this.setMammal(true);
        this.setStorage(54);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("front_flipper", "hind_flipper", "inner_teeth", "ribcage", "skull", "spine", "tail_fluke", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Main head");
    }
}
