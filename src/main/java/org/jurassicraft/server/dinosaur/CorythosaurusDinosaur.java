package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.CorythosaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class CorythosaurusDinosaur extends Dinosaur
{
    public CorythosaurusDinosaur()
    {
        super();

        this.setName("Corythosaurus");
        this.setDinosaurClass(CorythosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xBAA87E, 0x5E7201);
        this.setEggColorFemale(0xB3A27D, 0xE9BF47);
        this.setHealth(10, 30);
        this.setSpeed(0.46, 0.41);
        this.setStrength(5, 15);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 1.9F);
        this.setSizeX(0.4F, 1.8F);
        this.setSizeY(0.6F, 2.8F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("skull", "cheek_teeth", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(1.75F, 0.35F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.disableRegistry();
    }
}
