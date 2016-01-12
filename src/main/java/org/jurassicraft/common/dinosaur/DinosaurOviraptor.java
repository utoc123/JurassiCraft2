package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityOviraptor;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurOviraptor extends Dinosaur
{
    public DinosaurOviraptor()
    {
        super();

        this.setName("Oviraptor");
        this.setDinosaurClass(EntityOviraptor.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xA2A7AE, 0x666E81);
        this.setEggColorFemale(0xDEDAC4, 0x663341);
        this.setHealth(16, 5);
        this.setSpeed(0.11, 0.10);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.32F, 0.95F);
        this.setSizeX(0.25F, 0.6F);
        this.setSizeY(0.32F, 0.95F);
        this.disableRegistry();
        this.setStorage(9);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones();
    }
}
