package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.DimorphodonEntity;
import org.jurassicraft.server.period.TimePeriod;

public class DimorphodonDinosaur extends Dinosaur
{
    public DimorphodonDinosaur()
    {
        super();

        this.setName("Dimorphodon");
        this.setDinosaurClass(DimorphodonEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xB2AC94, 0x636644);
        this.setEggColorFemale(0xBDB4A9, 0x726B57);
        this.setHealth(5, 10);
        this.setSpeed(0.35, 0.30);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.25F, 0.7F);
        this.setSizeX(0.25F, 0.5F);
        this.setSizeY(0.25F, 0.75F);
        this.setStorage(9);
        this.setDiet(Diet.CARNIVORE);
        this.setBones("skull", "teeth", "leg_bones", "neck", "ribs_and_spine", "shoulder_blade", "tail_vertebrae", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(0.5F, 0.15F);
        this.disableRegistry();
    }
}
