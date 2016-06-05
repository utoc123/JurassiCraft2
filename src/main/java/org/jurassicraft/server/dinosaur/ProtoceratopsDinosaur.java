package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.ProtoceratopsEntity;
import org.jurassicraft.server.period.TimePeriod;

public class ProtoceratopsDinosaur extends Dinosaur
{
    public ProtoceratopsDinosaur()
    {
        super();

        this.setName("Protoceratops");
        this.setDinosaurClass(ProtoceratopsEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xFDCEB5, 0xFBC073);
        this.setEggColorFemale(0xEBCC98, 0xAA804E);
        this.setHealth(10, 30);
        this.setSpeed(0.46, 0.40);
        this.setStrength(5, 10);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 0.85F);
        this.setSizeX(0.3F, 1.0F);
        this.setSizeY(0.4F, 1.25F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(1.2F, 0.35F);
        this.disableRegistry();
    }
}
