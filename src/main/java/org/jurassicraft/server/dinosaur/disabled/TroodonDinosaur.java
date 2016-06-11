package org.jurassicraft.server.dinosaur.disabled;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.base.SleepingSchedule;
import org.jurassicraft.server.entity.dinosaur.disabled.TroodonEntity;
import org.jurassicraft.server.period.TimePeriod;

public class TroodonDinosaur extends Dinosaur
{
    public TroodonDinosaur()
    {
        super();

        this.setName("Troodon");
        this.setDinosaurClass(TroodonEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x9DAA7A, 0x636B67);
        this.setEggColorFemale(0xA2A67C, 0x646D66);
        this.setHealth(5, 20);
        this.setSpeed(0.46, 0.40);
        this.setStrength(1, 15);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.3F, 0.95F);
        this.setSizeX(0.3F, 0.7F);
        this.setSizeY(0.4F, 0.95F);
        this.setStorage(18);
        this.setDiet(Diet.CARNIVORE);
        this.setSleepingSchedule(SleepingSchedule.NOCTURNAL);
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head UPPER");
        this.setScale(0.75F, 0.25F);
        this.setOffset(0.0F, 0.0F, 0.5F);
        this.disableRegistry();
    }
}
