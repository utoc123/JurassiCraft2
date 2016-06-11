package org.jurassicraft.server.dinosaur.disabled;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.disabled.LeptictidiumEntity;
import org.jurassicraft.server.period.TimePeriod;

public class LeptictidiumDinosaur extends Dinosaur
{
    public LeptictidiumDinosaur()
    {
        super();

        this.setName("Leptictidium");
        this.setDinosaurClass(LeptictidiumEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS); // TODO EOCENE
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
        this.setDiet(Diet.HERBIVORE);
        this.setBones();
        this.setScale(0.6F, 0.25F);
        this.disableRegistry();
    }
}
