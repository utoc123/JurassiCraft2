package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.OviraptorEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class OviraptorDinosaur extends Dinosaur
{
    public OviraptorDinosaur()
    {
        super();

        this.setName("Oviraptor");
        this.setDinosaurClass(OviraptorEntity.class);
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
        this.setHeadCubeName("Head");
        this.setScale(0.65F, 0.18F);
        this.setOffset(0.0F, 0.0F, -0.4F);
        this.disableRegistry();
    }
}
