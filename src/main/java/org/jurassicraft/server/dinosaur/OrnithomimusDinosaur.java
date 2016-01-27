package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.OrnithomimusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class OrnithomimusDinosaur extends Dinosaur
{
    public OrnithomimusDinosaur()
    {
        super();

        this.setName("Ornithomimus");
        this.setDinosaurClass(OrnithomimusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x92A8D5, 0x475F93);
        this.setEggColorFemale(0xBDC4A9, 0x7F91C1);
        this.setHealth(16, 65);
        this.setSpeed(0.52, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.58F, 1.95F);
        this.setSizeX(0.2F, 1.0F);
        this.setSizeY(0.45F, 1.55F);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head Base");
    }
}
