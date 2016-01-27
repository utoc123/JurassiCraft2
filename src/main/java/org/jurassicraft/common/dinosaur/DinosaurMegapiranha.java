package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityMegapiranha;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurMegapiranha extends Dinosaur
{
    public DinosaurMegapiranha()
    {
        super();

        this.setName("Megapiranha");
        this.setDinosaurClass(EntityMegapiranha.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS); // TODO LATE MIOCENE
        this.setEggColorMale(0x17100B, 0x645C54);
        this.setEggColorFemale(0x7D735D, 0x322922);
        this.setHealth(10, 30);
        this.setSpeed(0.62, 0.50);
        this.setStrength(2, 6);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.35F, 0.35F);//TODO uh?
        this.setSizeX(0.15F, 0.5F);
        this.setSizeY(0.15F, 0.7F);
        this.setMarineAnimal(true);
        this.setStorage(18);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("anal_fin", "body_fins", "caudal_fin", "dorsal_fin", "skull", "spine", "teeth");
        this.setHeadCubeName("Neck ");
    }
}
