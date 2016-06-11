package org.jurassicraft.server.dinosaur.disabled;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.disabled.BaryonyxEntity;
import org.jurassicraft.server.period.TimePeriod;

public class BaryonyxDinosaur extends Dinosaur
{
    public BaryonyxDinosaur()
    {
        super();

        this.setName("Baryonyx");
        this.setDinosaurClass(BaryonyxEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x567F4F, 0x13270F);
        this.setEggColorFemale(0x9D9442, 0x2A2405);
        this.setHealth(5, 30);
        this.setSpeed(0.45, 0.40);
        this.setStrength(1, 10);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.55F, 2.95F);
        this.setSizeX(0.35F, 1.5F);
        this.setSizeY(0.55F, 2.95F);
        this.setStorage(36);
        this.setDiet(Diet.CARNIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "claw", "arm_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.25F);
        this.disableRegistry();
    }
}
