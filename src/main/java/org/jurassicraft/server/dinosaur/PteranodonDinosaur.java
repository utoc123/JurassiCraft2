package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.PteranodonEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class PteranodonDinosaur extends Dinosaur
{
    public PteranodonDinosaur()
    {
        super();

        this.setName("Pteranodon");
        this.setDinosaurClass(PteranodonEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x57504C, 0x24383F);
        this.setEggColorFemale(0x535F65, 0x56312C);
        this.setHealth(10, 20);
        this.setStrength(5, 20);
        this.setSpeed(0.46, 0.40);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.8F, 2.0F);
        this.setSizeY(0.6F, 1.8F);
        this.setStorage(27);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("leg_bones", "neck_vertebrae", "pelvis", "ribcage", "skull", "tail_vertebrae", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.2F, 0.3F);
    }
}
