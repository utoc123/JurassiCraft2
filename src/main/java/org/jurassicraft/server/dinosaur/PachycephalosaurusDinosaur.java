package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.PachycephalosaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class PachycephalosaurusDinosaur extends Dinosaur
{
    public PachycephalosaurusDinosaur()
    {
        super();

        this.setName("Pachycephalosaurus");
        this.setDinosaurClass(PachycephalosaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD9E1C0, 0x91501F);
        this.setEggColorFemale(0x8E805E, 0x4A5154);
        this.setHealth(10, 50);
        this.setSpeed(0.46, 0.40);
        this.setStrength(10, 40);
        this.setMaximumAge(fromDays(40));
        this.setSizeX(0.25F, 1.25F);
        this.setSizeY(0.5F, 2.3F);
        this.setStorage(27);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(0.9F, 0.2F);
        this.setEyeHeight(0.4F, 1.8F);
        this.disableRegistry();
    }
}
