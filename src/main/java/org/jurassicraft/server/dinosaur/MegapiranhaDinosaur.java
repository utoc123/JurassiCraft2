package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.MegapiranhaEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class MegapiranhaDinosaur extends Dinosaur
{
    public MegapiranhaDinosaur()
    {
        super();

        this.setName("Megapiranha");
        this.setDinosaurClass(MegapiranhaEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS); // TODO LATE MIOCENE
        this.setEggColorMale(0x17100B, 0x645C54);
        this.setEggColorFemale(0x7D735D, 0x322922);
        this.setHealth(1, 10);
        this.setSpeed(0.62, 0.50);
        this.setStrength(5, 10);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.35F, 0.35F);//TODO uh?
        this.setSizeX(0.15F, 0.5F);
        this.setSizeY(0.15F, 0.7F);
        this.setMarineAnimal(true);
        this.setStorage(18);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("anal_fin", "body_fins", "caudal_fin", "dorsal_fin", "skull", "spine", "teeth");
        this.setHeadCubeName("Neck ");
        this.setScale(1.0F, 0.15F);
        this.setOffset(0.0F, 0.65F, -0.25F);
    }
}
