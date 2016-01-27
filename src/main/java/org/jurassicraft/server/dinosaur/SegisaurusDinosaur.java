package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.SegisaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.entity.base.EnumSleepingSchedule;
import org.jurassicraft.server.period.EnumTimePeriod;

public class SegisaurusDinosaur extends Dinosaur
{
    public SegisaurusDinosaur()
    {
        super();

        this.setName("Segisaurus");
        this.setDinosaurClass(SegisaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0x834B4C, 0x4B8FB5);
        this.setEggColorFemale(0xCEEE99, 0x776343);
        this.setHealth(16, 30);
        this.setStrength(6, 36);
        this.setSpeed(0.46, 0.40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.3F, 0.85F);
        this.setSizeX(0.3F, 0.5F);
        this.setSizeY(0.4F, 0.85F);
        this.setStorage(9);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setSleepingSchedule(EnumSleepingSchedule.NOCTURNAL);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("head");
        this.setScale(0.55F, 0.2F);
    }
}
