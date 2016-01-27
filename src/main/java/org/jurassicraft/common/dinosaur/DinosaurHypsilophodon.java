package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityHypsilophodon;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurHypsilophodon extends Dinosaur
{
    public DinosaurHypsilophodon()
    {
        super();

        this.setName("Hypsilophodon");
        this.setDinosaurClass(EntityHypsilophodon.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x7DAC78, 0x3E6226);
        this.setEggColorFemale(0x799073, 0x33432F);
        this.setHealth(10, 25);
        this.setSpeed(0.35, 0.30);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.2F, 0.7F);
        this.setSizeX(0.2F, 0.6F);
        this.setSizeY(0.25F, 0.85F);
        this.setStorage(9);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "arm_bones");
        this.setHeadCubeName("Head ");
    }
}
