package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.MicroceratusEntity;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class MicroceratusDinosaur extends Dinosaur
{
    public MicroceratusDinosaur()
    {
        super();

        this.setName("Microceratus");
        this.setDinosaurClass(MicroceratusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x956F2D, 0x92442C);
        this.setEggColorFemale(0x958331, 0x7E4A1F);
        this.setHealth(3, 10);
        this.setSpeed(0.41, 0.35);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.14F, 0.36F);
        this.setSizeX(0.15F, 0.4F);
        this.setSizeY(0.18F, 0.55F);
        this.setStorage(9);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(0.5F, 0.18F);
        this.disableRegistry();
    }
}
