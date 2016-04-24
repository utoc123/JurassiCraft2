package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.CoelacanthEntity;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class CoelacanthDinosaur extends Dinosaur
{
    public CoelacanthDinosaur()
    {
        super();

        this.setName("Coelacanth");
        this.setDinosaurClass(CoelacanthEntity.class);
        this.setTimePeriod(EnumTimePeriod.DEVONIAN);
        this.setEggColorMale(0x3C4B65, 0x737E96);
        this.setEggColorFemale(0x4C4A3A, 0x7C775E);
        this.setHealth(5, 10);
        this.setStrength(1, 5);
        this.setSpeed(0.62, 0.50);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.2F, 0.6F);
        this.setSizeX(0.3F, 1.2F);
        this.setSizeY(0.3F, 1.0F);
        this.setMarineAnimal(true);
        this.setStorage(18);
        this.setDiet(Diet.PISCIVORE);
        this.setBones("skull", "teeth", "spine", "second_dorsal_fin", "pelvic_fin_bones", "pectoral_fin_bones", "first_dorsal_fin", "caudal_fin", "anal_fin");
        this.setHeadCubeName("Head joint");
        this.setScale(2.1F, 0.55F);
        this.setOffset(0.0F, 1.0F, -0.25F);
        this.disableRegistry();
    }
}
