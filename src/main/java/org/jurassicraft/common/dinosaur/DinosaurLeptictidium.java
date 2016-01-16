package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityLeptictidium;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurLeptictidium extends Dinosaur
{
    public DinosaurLeptictidium()
    {
        super();

        this.setName("Leptictidium");
        this.setDinosaurClass(EntityLeptictidium.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS); // TODO EOCENE
        this.setEggColorMale(0x362410, 0x978A78);
        this.setEggColorFemale(0xAFA27E, 0x3E2D17);
        this.setHealth(8, 18);
        this.setStrength(6, 36);
        this.setSpeed(0.42, 0.38);
        this.setMaximumAge(25);
        this.setEyeHeight(0.21F, 0.63F);
        this.setSizeX(0.2F, 0.5F);
        this.setSizeY(0.25F, 0.75F);
        this.setMammal(true);
        this.disableRegistry();
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones();
    }
}
