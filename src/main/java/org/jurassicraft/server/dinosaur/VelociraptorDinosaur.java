package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.VelociraptorEntity;
import org.jurassicraft.server.period.TimePeriod;

public class VelociraptorDinosaur extends Dinosaur
{
    public VelociraptorDinosaur()
    {
        super();

        this.setName("Velociraptor");
        this.setDinosaurClass(VelociraptorEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xB17041, 0x3B1505);
        this.setEggColorFemale(0x91765D, 0x5A4739);
        this.setSpeed(0.35, 0.40);
        this.setAttackSpeed(1.2);
        this.setHealth(10, 40);
        this.setStrength(1, 8);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.7F);
        this.setSizeX(0.5F, 1.0F);
        this.setSizeY(0.5F, 1.8F);
        this.setStorage(27);
        this.setDiet(Diet.CARNIVORE);
        this.setBones("claw", "tooth", "skull");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.3F);
        this.setImprintable(true);
        this.setDefendOwner(true);
    }
}
