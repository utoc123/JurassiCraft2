package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.CompsognathusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class CompsognathusDinosaur extends Dinosaur
{
    public CompsognathusDinosaur()
    {
        super();

        this.setName("Compsognathus");
        this.setDinosaurClass(CompsognathusEntity.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0x7B8042, 0x454B3B);
        this.setEggColorFemale(0x7D734A, 0x484A3D);
        this.setHealth(2, 5);
        this.setSpeed(0.3, 0.2);
        this.setStrength(1, 3);
        this.setMaximumAge(fromDays(20));
        this.setEyeHeight(0.2F, 0.5F);
        this.setSizeX(0.1F, 0.3F);
        this.setSizeY(0.25F, 0.55F);
        this.setStorage(9);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "teeth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "arm_bones");
        this.setHeadCubeName("Head");
        this.setScale(0.1F, 0.04F);
        this.setOffset(0.0F, -12.0F, -0.8F);
    }
}
