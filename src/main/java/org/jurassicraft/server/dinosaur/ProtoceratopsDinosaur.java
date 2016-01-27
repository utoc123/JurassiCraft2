package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.ProtoceratopsEntity;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.period.EnumTimePeriod;

public class ProtoceratopsDinosaur extends Dinosaur
{
    public ProtoceratopsDinosaur()
    {
        super();

        this.setName("Protoceratops");
        this.setDinosaurClass(ProtoceratopsEntity.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0xFDCEB5, 0xFBC073);
        this.setEggColorFemale(0xEBCC98, 0xAA804E);
        this.setHealth(16, 55);
        this.setSpeed(0.46, 0.40);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 0.85F);
        this.setSizeX(0.3F, 1.0F);
        this.setSizeY(0.4F, 1.25F);
        this.setStorage(27);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
    }
}
