package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.HerrerasaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class HerrerasaurusDinosaur extends Dinosaur
{
    public HerrerasaurusDinosaur()
    {
        super();

        this.setName("Herrerasaurus");
        this.setDinosaurClass(HerrerasaurusEntity.class);
        this.setTimePeriod(TimePeriod.TRIASSIC);
        this.setEggColorMale(0x2B1919, 0x932C23);
        this.setEggColorFemale(0x7C6F44, 0x2B2721);
        this.setHealth(10, 40);
        this.setSpeed(0.45, 0.40);
        this.setStrength(5, 15);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 2.25F);
        this.setSizeX(0.4F, 1.8F);
        this.setSizeY(0.55F, 2.55F);
        this.setStorage(36);
        this.setDiet(Diet.CARNIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "foot_bones", "arm_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.25F);
        this.disableRegistry();
    }
}
