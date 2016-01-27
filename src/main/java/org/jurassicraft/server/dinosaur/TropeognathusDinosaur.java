package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.TropeognathusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class TropeognathusDinosaur extends Dinosaur
{
    public TropeognathusDinosaur()
    {
        super();

        this.setName("Tropeognathus");
        this.setDinosaurClass(TropeognathusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x4E646B, 0x483141);
        this.setEggColorFemale(0x5C6C71, 0x4D3E4D);
        this.setHealth(12, 55);
        this.setSpeed(0.46, 0.30);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.45F, 1.45F);
        this.setSizeX(0.35F, 1.5F);
        this.setSizeY(0.45F, 1.55F);
        this.setStorage(27);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("leg_bones", "pelvis", "skull", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
    }
}
