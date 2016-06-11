package org.jurassicraft.server.dinosaur.disabled;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.disabled.TylosaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class TylosaurusDinosaur extends Dinosaur
{
    public TylosaurusDinosaur()
    {
        super();

        this.setName("Tylosaurus");
        this.setDinosaurClass(TylosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x187D75, 0x15544F);
        this.setEggColorFemale(0x798A8F, 0x101517);
        this.setHealth(10, 40);
        this.setSpeed(0.85, 0.75);
        this.setStrength(5, 30);
        this.setMaximumAge(fromDays(60));
        this.setEyeHeight(0.35F, 2.35F);
        this.setSizeX(0.85F, 4.5F);
        this.setSizeY(0.55F, 2.95F);
        this.setMarineAnimal(true);
        this.setMammal(true);
        this.setStorage(54);
        this.setDiet(Diet.CARNIVORE);
        this.setBones("front_flipper", "hind_flipper", "inner_teeth", "ribcage", "skull", "spine", "tail_fluke", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Main head");
        this.setScale(2.2F, 0.45F);
        this.setOffset(0.0F, 0.0F, 1.0F);
        this.disableRegistry();
    }
}
