package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.TherizinosaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.entity.base.EnumSleepingSchedule;
import org.jurassicraft.server.period.EnumTimePeriod;

public class TherizinosaurusDinosaur extends Dinosaur
{
    public TherizinosaurusDinosaur()
    {
        super();

        this.setName("Therizinosaurus");
        this.setDinosaurClass(TherizinosaurusEntity.class);
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
        this.setSleepingSchedule(EnumSleepingSchedule.DIURNAL);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
<<<<<<< HEAD
        this.setUsePosesForWalkingAnim(true);
=======
        this.setScale(3.5F, 0.55F);
        this.setOffset(0.0F, 1.0F, 0.0F);
>>>>>>> 984f5030a1d7f3567fe6794da5737912cb2b3dc8
    }
}
