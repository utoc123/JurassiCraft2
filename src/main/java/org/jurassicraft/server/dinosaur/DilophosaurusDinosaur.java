package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.DilophosaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.entity.base.EnumSleepingSchedule;
import org.jurassicraft.server.period.EnumTimePeriod;

public class DilophosaurusDinosaur extends Dinosaur
{
    public DilophosaurusDinosaur()
    {
        super();

        this.setName("Dilophosaurus");
        this.setDinosaurClass(DilophosaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0x62702B, 0x26292A);
        this.setEggColorFemale(0x5E6E2B, 0x2D221A);
        this.setHealth(10, 30);
        this.setSpeed(0.42, 0.40);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.45F, 2.15F);
        this.setSizeX(0.3F, 1.25F);
        this.setSizeY(0.5F, 2.5F);
        this.setStorage(27);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setSleepingSchedule(EnumSleepingSchedule.CREPUSCULAR);
        this.setBones("skull", "tooth", "arm_bones", "leg_bones", "neck", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
    }
}
