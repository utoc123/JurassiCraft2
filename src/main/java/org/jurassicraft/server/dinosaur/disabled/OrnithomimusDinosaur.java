package org.jurassicraft.server.dinosaur.disabled;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.disabled.OrnithomimusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class OrnithomimusDinosaur extends Dinosaur
{
    public OrnithomimusDinosaur()
    {
        super();

        this.setName("Ornithomimus");
        this.setDinosaurClass(OrnithomimusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x92A8D5, 0x475F93);
        this.setEggColorFemale(0xBDC4A9, 0x7F91C1);
        this.setHealth(5, 25);
        this.setSpeed(0.52, 0.40);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.58F, 1.95F);
        this.setSizeX(0.2F, 1.0F);
        this.setSizeY(0.45F, 1.55F);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head Base");
        this.setScale(0.9F, 0.25F);
        this.disableRegistry();
    }
}
