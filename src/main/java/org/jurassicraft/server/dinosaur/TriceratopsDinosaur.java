package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.TriceratopsEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class TriceratopsDinosaur extends Dinosaur
{
    public TriceratopsDinosaur()
    {
        super();

        this.setName("Triceratops");
        this.setDinosaurClass(TriceratopsEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x404138, 0x1C1C1C);
        this.setEggColorFemale(0x8F7B76, 0x73676A);
        this.setHealth(16, 55);
        this.setSpeed(0.37, 0.35);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.8F);
        this.setSizeX(0.35F, 2.5F);
        this.setSizeY(0.6F, 2.8F);
        this.setStorage(36);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("front_leg_bones", "hind_leg_bones", "horn", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.8F, 0.325F);
        this.setOffset(0.0F, 0.75F, 0.0F);
    }
}
