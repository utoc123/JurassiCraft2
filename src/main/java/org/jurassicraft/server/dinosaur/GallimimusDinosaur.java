package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.GallimimusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class GallimimusDinosaur extends Dinosaur
{
    public GallimimusDinosaur()
    {
        super();

        this.setName("Gallimimus");
        this.setDinosaurClass(GallimimusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xC57B5F, 0x985E54);
        this.setEggColorFemale(0xDAC0AC, 0x966943);
        this.setHealth(5, 25);
        this.setSpeed(0.3, 0.40);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.58F, 3F);
        this.setSizeX(0.3F, 1.5F);
        this.setSizeY(0.65F, 3.25F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("skull", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "foot_bones", "arm_bones");
        this.setHeadCubeName("Head Base");
        this.setScale(1.2F, 0.25F);
        this.setFlee(true);
    }
}
