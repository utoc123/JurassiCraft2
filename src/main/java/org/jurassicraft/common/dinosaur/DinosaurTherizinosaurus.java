package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityTherizinosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.entity.base.EnumSleepingSchedule;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurTherizinosaurus extends Dinosaur
{
    public DinosaurTherizinosaurus()
    {
        super();

        this.setName("Therizinosaurus");
        this.setDinosaurClass(EntityTherizinosaurus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x787878, 0x2B2B2B);
        this.setEggColorFemale(0x7F7F7F, 0x272727);
        this.setHealth(15, 75);
        this.setSpeed(0.45, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(65));
        this.setEyeHeight(0.95F, 5.85F);
        this.setSizeX(0.65F, 2.25F);
        this.setSizeY(1.0F, 5.95F);
        this.setStorage(36);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setSleepingSchedule(EnumSleepingSchedule.NOCTURNAL);
        this.setBones("skull", "teeth");
    }
}
