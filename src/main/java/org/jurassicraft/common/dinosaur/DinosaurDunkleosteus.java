package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityDunkleosteus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.entity.base.EnumSleepingSchedule;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurDunkleosteus extends Dinosaur
{
    public DinosaurDunkleosteus()
    {
        super();

        this.setName("Dunkleosteus");
        this.setDinosaurClass(EntityDunkleosteus.class);
        this.setTimePeriod(EnumTimePeriod.DEVONIAN);
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
        this.setDiet(EnumDiet.CARNIVORE);
        this.setSleepingSchedule(EnumSleepingSchedule.DIURNAL);
        this.setBones("skull", "mouth_plates");
        this.setHeadCubeName("Main head");
    }
}
