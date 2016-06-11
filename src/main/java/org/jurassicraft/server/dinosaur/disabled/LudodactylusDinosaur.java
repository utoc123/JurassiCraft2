package org.jurassicraft.server.dinosaur.disabled;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.disabled.LudodactylusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class LudodactylusDinosaur extends Dinosaur
{
    public LudodactylusDinosaur()
    {
        super();

        this.setName("Ludodactylus");
        this.setDinosaurClass(LudodactylusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x565656, 0x1D1E20);
        this.setEggColorFemale(0x884D3E, 0x35302B);
        this.setHealth(5, 15);
        this.setSpeed(0.47, 0.40);
        this.setStrength(3, 10);
        this.setMaximumAge(40);
        this.setEyeHeight(0.48F, 1.25F);
        this.setSizeX(0.4F, 1.0F);
        this.setSizeY(0.55F, 1.35F);
        this.setStorage(18);
        this.setDiet(Diet.CARNIVORE);
        this.setBones("leg_bones", "pelvis", "skull", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.35F);
        this.disableRegistry();
    }
}
