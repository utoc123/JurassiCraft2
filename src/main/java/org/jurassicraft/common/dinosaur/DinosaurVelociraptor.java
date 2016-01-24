package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityVelociraptor;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurVelociraptor extends Dinosaur
{
    public DinosaurVelociraptor()
    {
        super();

        this.setName("Velociraptor");
        this.setDinosaurClass(EntityVelociraptor.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xB17041, 0x3B1505);
        this.setEggColorFemale(0x91765D, 0x5A4739);
        this.setAttackSpeed(2.0);
        this.setHealth(16, 65);
        this.setSpeed(0.48, 0.40);
        this.setStrength(6, 21);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.7F);
        this.setSizeX(0.5F, 1.0F);
        this.setSizeY(0.5F, 1.8F);
        this.setStorage(27);
        this.setOverlayCount(1);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("claw", "tooth", "skull");
    }

    @Override
    public boolean useAllGrowthStages()
    {
        return true;
    }
}
