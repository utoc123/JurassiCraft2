package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.DodoEntity;
import org.jurassicraft.server.period.TimePeriod;

public class DodoDinosaur extends Dinosaur
{
    public DodoDinosaur()
    {
        super();

        this.setName("Dodo");
        this.setDinosaurClass(DodoEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xA2996E, 0x545338);
        this.setEggColorFemale(0x908B80, 0x665C51);
        this.setHealth(5, 15);
        this.setSpeed(0.35, 0.30);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(20));
        this.setEyeHeight(0.35F, 0.95F);
        this.setSizeX(0.25F, 0.5F);
        this.setSizeY(0.35F, 0.95F);
        this.setStorage(9);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("skull");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.3F);
        this.disableRegistry();
    }
}
