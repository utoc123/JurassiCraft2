package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.ChasmosaurusEntity;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class ChasmosaurusDinosaur extends Dinosaur
{
    public ChasmosaurusDinosaur()
    {
        super();

        this.setName("Chasmosaurus");
        this.setDinosaurClass(ChasmosaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xB6B293, 0x85563E);
        this.setEggColorFemale(0xB9B597, 0x323232);
        this.setHealth(20, 40);
        this.setSpeed(0.37, 0.35);
        this.setStrength(5, 15);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.3F, 1.35F);
        this.setSizeX(0.35F, 1.75F);
        this.setSizeY(0.45F, 2.35F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "hind_leg_bones", "front_leg_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.55F, 0.3F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.disableRegistry();
    }
}
