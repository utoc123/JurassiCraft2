package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.MajungasaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class MajungasaurusDinosaur extends Dinosaur
{
    public MajungasaurusDinosaur()
    {
        super();

        this.setName("Majungasaurus");
        this.setDinosaurClass(MajungasaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE6CC9B, 0x7C8A7D);
        this.setEggColorFemale(0xE8CF9C, 0xADAC7E);
        this.setHealth(10, 40);
        this.setStrength(5, 20);
        this.setSpeed(0.48, 0.40);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.6F, 2.6F);
        this.setSizeX(0.5F, 2.25F);
        this.setSizeY(0.8F, 3.0F);
        this.setStorage(36);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.6F, 0.4F);
        this.disableRegistry();
    }
}