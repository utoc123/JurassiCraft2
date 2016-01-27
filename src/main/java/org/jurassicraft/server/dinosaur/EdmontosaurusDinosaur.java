package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.EdmontosaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class EdmontosaurusDinosaur extends Dinosaur
{
    public EdmontosaurusDinosaur()
    {
        super();

        this.setName("Edmontosaurus");
        this.setDinosaurClass(EdmontosaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xB97840, 0x644329);
        this.setEggColorFemale(0x8F8039, 0x615A30);
        this.setHealth(10, 40);
        this.setStrength(5, 20);
        this.setSpeed(0.46, 0.41);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.55F, 3.45F);
        this.setSizeX(0.5F, 2.8F);
        this.setSizeY(0.8F, 4.25F);
        this.setStorage(45);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("cheek_teeth");
        this.setHeadCubeName("Head");
    }
}
