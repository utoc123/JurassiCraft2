package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityStegosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurStegosaurus extends Dinosaur
{
    public DinosaurStegosaurus()
    {
        super();

        this.setName("Stegosaurus");
        this.setDinosaurClass(EntityStegosaurus.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0xBABF83, 0x75964E);
        this.setEggColorFemale(0xC8BC9A, 0x827D54);
        this.setHealth(16, 55);
        this.setSpeed(0.38, 0.32);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.26F, 2.2F);
        this.setSizeX(0.5F, 4.0F);
        this.setSizeY(0.7F, 4.8F);
        this.setStorage(36);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail", "thagomizer", "tooth");
        this.setHeadCubeName("Head");
    }
}
