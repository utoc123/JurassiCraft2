package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.LambeosaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class LambeosaurusDinosaur extends Dinosaur
{
    public LambeosaurusDinosaur()
    {
        super();

        this.setName("Lambeosaurus");
        this.setDinosaurClass(LambeosaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x82947A, 0x2F3129);
        this.setEggColorFemale(0x898969, 0x464C3A);
        this.setHealth(10, 50);
        this.setSpeed(0.46, 0.41);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.5F, 3.45F);
        this.setSizeX(0.5F, 2.8F);
        this.setSizeY(0.8F, 4.25F);
        this.setStorage(45);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "cheek_teeth", "shoulder", "tail_vertebrae", "ribcage", "pelvis", "neck_vertebrae", "hind_leg_bones", "front_leg_bones");
        this.setHeadCubeName("Head");
        this.setScale(2.5F, 0.45F);
        this.setOffset(0.0F, 0.775F, 0.0F);
    }
}