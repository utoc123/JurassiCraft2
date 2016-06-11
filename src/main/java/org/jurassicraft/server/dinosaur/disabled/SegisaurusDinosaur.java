package org.jurassicraft.server.dinosaur.disabled;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.base.SleepingSchedule;
import org.jurassicraft.server.entity.dinosaur.disabled.SegisaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class SegisaurusDinosaur extends Dinosaur
{
    public SegisaurusDinosaur()
    {
        super();

        this.setName("Segisaurus");
        this.setDinosaurClass(SegisaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x834B4C, 0x4B8FB5);
        this.setEggColorFemale(0xCEEE99, 0x776343);
        this.setHealth(5, 10);
        this.setStrength(3, 7);
        this.setSpeed(0.46, 0.40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.3F, 0.85F);
        this.setSizeX(0.3F, 0.5F);
        this.setSizeY(0.4F, 0.85F);
        this.setStorage(9);
        this.setDiet(Diet.CARNIVORE);
        this.setSleepingSchedule(SleepingSchedule.NOCTURNAL);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("head");
        this.setScale(0.55F, 0.2F);
        this.disableRegistry();
    }
}
