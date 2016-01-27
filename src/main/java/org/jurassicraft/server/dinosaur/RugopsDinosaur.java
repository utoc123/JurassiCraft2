package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.RugopsEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class RugopsDinosaur extends Dinosaur
{
    public RugopsDinosaur()
    {
        super();

        this.setName("Rugops");
        this.setDinosaurClass(RugopsEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xB5F75D, 0x202022);
        this.setEggColorFemale(0xE1A857, 0x5A2108);
        this.setHealth(16, 65);
        this.setSpeed(0.47, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.6F, 2.05F);
        this.setSizeX(0.50F, 1.5F);
        this.setSizeY(0.8F, 2.6F);
        this.setStorage(36);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.0F, 0.3F);
    }
}
