package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.StegosaurusEntity;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class StegosaurusDinosaur extends Dinosaur
{
    public StegosaurusDinosaur()
    {
        super();

        this.setName("Stegosaurus");
        this.setDinosaurClass(StegosaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0xBABF83, 0x75964E);
        this.setEggColorFemale(0xC8BC9A, 0x827D54);
        this.setHealth(10, 50);
        this.setStrength(10, 25);
        this.setSpeed(0.38, 0.32);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.26F, 2.2F);
        this.setSizeX(0.5F, 4.0F);
        this.setSizeY(0.7F, 4.8F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail", "thagomizer", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.55F, 0.35F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.disableRegistry();
    }
}
