package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.MamenchisaurusEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class MamenchisaurusDinosaur extends Dinosaur
{
    public MamenchisaurusDinosaur()
    {
        super();

        this.setName("Mamenchisaurus");
        this.setDinosaurClass(MamenchisaurusEntity.class);
        this.setTimePeriod(EnumTimePeriod.JURASSIC);
        this.setEggColorMale(0xD1BA49, 0x909B1D);
        this.setEggColorFemale(0x98764E, 0x545028);
        this.setHealth(25, 140);
        this.setSpeed(0.32, 0.25);
        this.setStorage(52);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(95));
        this.setEyeHeight(1.55F, 13.95F);
        this.setSizeX(0.75F, 5.5F);
        this.setSizeY(0.75F, 5.95F);
        this.setBones("skull");
        this.setHeadCubeName("Head");
    }
}
