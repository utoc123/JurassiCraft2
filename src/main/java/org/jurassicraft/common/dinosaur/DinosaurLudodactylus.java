package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityLudodactylus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurLudodactylus extends Dinosaur
{
    public DinosaurLudodactylus()
    {
        super();

        this.setName("Ludodactylus");
        this.setDinosaurClass(EntityLudodactylus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x565656, 0x1D1E20);
        this.setEggColorFemale(0x884D3E, 0x35302B);
        this.setHealth(16, 40);
        this.setSpeed(0.47, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(40);
        this.setEyeHeight(0.48F, 1.25F);
        this.setSizeX(0.4F, 1.0F);
        this.setSizeY(0.55F, 1.35F);
        this.setStorage(18);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("leg_bones", "pelvis", "skull", "tail_vertebrae", "teeth", "wing_bones");
    }
}
