package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.TriceratopsEntity;
import org.jurassicraft.server.period.TimePeriod;

public class TriceratopsDinosaur extends Dinosaur
{
    public TriceratopsDinosaur()
    {
        super();

        this.setName("Triceratops");
        this.setDinosaurClass(TriceratopsEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x404138, 0x1C1C1C);
        this.setEggColorFemale(0x8F7B76, 0x73676A);
        this.setSpeed(0.37, 0.35);
        this.setAttackSpeed(1.3);
        this.setHealth(10, 50);
        this.setStrength(2, 10);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.8F);
        this.setSizeX(0.35F, 2.5F);
        this.setSizeY(0.6F, 3.0F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("front_leg_bones", "hind_leg_bones", "horn", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.35F, 0.325F);
        this.setOffset(0.0F, 0.45F, 0.0F);
        this.setImprintable(true);
        this.setDefendOwner(true);
    }
}
