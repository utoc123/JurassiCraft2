package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.base.SleepingSchedule;
import org.jurassicraft.server.entity.dinosaur.DunkleosteusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class DunkleosteusDinosaur extends Dinosaur
{
    public DunkleosteusDinosaur()
    {
        super();

        this.setName("Dunkleosteus");
        this.setDinosaurClass(DunkleosteusEntity.class);
        this.setTimePeriod(TimePeriod.DEVONIAN);
        this.setEggColorMale(0xA89B8C, 0x753A28);
        this.setEggColorFemale(0xA6A588, 0x785F2A);
        this.setHealth(15, 60);
        this.setSpeed(0.52, 0.45);
        this.setStrength(10, 40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.3F, 1.2F);
        this.setSizeX(0.8F, 2.7F);
        this.setSizeY(0.5F, 2.0F);
        this.setMarineAnimal(true);
        this.setStorage(27);
        this.setDiet(Diet.CARNIVORE);
        this.setSleepingSchedule(SleepingSchedule.DIURNAL);
        this.setBones("skull", "mouth_plates");
        this.setHeadCubeName("Main head");
        this.setScale(2.1F, 0.55F);
        this.setOffset(0.0F, 1.0F, -0.25F);
        this.disableRegistry();
    }
}
