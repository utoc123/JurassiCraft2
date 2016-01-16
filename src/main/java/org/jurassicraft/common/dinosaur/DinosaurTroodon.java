package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityTroodon;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.entity.base.EnumSleepingSchedule;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurTroodon extends Dinosaur
{
    public DinosaurTroodon()
    {
        super();

        this.setName("Troodon");
        this.setDinosaurClass(EntityTroodon.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x9DAA7A, 0x636B67);
        this.setEggColorFemale(0xA2A67C, 0x646D66);
        this.setHealth(16, 30);
        this.setSpeed(0.46, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.3F, 0.95F);
        this.setSizeX(0.3F, 0.7F);
        this.setSizeY(0.4F, 0.95F);
        this.setStorage(18);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setSleepingSchedule(EnumSleepingSchedule.NOCTURNAL);
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
    }
}
